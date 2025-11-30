package cc.kertaskerja.bontang.bidangurusan.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanAlreadyExistException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanDeleteForbiddenException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import cc.kertaskerja.bontang.program.domain.ProgramRepository;
import cc.kertaskerja.bontang.bidangurusan.web.BidangUrusanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidangUrusanServiceTest {

    @Mock
    private WebClient towerDataWebClient;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private BidangUrusanRepository bidangUrusanRepository;

    @Mock
    private ProgramRepository programRepository;

    private BidangUrusanService bidangUrusanService;

    @BeforeEach
    void setUp() {
        bidangUrusanService = new BidangUrusanService(towerDataWebClient, bidangUrusanRepository, programRepository);

        lenient().when(towerDataWebClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri("/bidangurusan/detail/findall")).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void findAll_returnsListFromTowerData() {
        List<BidangUrusanDto> towerResponse = List.of(new BidangUrusanDto(1L, "BU-001", "Bidang Infrastruktur"));
        mockTowerDataResponse(Mono.just(towerResponse));

        List<BidangUrusanDto> result = bidangUrusanService.findAll();

        assertEquals(towerResponse, result);
        verify(towerDataWebClient).get();
        verify(requestHeadersUriSpec).uri("/bidangurusan/detail/findall");
        verify(responseSpec).bodyToMono(any(ParameterizedTypeReference.class));
    }

    @Test
    void findAll_returnsEmptyList_whenTowerDataIsEmpty() {
        mockTowerDataResponse(Mono.empty());

        List<BidangUrusanDto> result = bidangUrusanService.findAll();

        assertEquals(List.of(), result);
        verify(towerDataWebClient).get();
    }

    @Test
    void findByKodeOpd_delegatesToRepository() {
        String kodeOpd = "OPD-01";
        List<BidangUrusan> bidangUrusanList = List.of(
                new BidangUrusan(1L, kodeOpd, "BU-001", "Bidang Infrastruktur", Instant.now(), Instant.now())
        );

        when(bidangUrusanRepository.findByKodeOpd(kodeOpd)).thenReturn(bidangUrusanList);

        Iterable<BidangUrusan> result = bidangUrusanService.findByKodeOpd(kodeOpd);

        assertEquals(bidangUrusanList, result);
        verify(bidangUrusanRepository).findByKodeOpd(kodeOpd);
        verifyNoInteractions(towerDataWebClient);
    }

    @Test
    void simpanBidangUrusan_savesData_whenTowerDataAvailableAndNotExists() {
        String kodeOpd = "OPD-01";
        String namaBidangUrusan = "Bidang Infrastruktur";
        BidangUrusanRequest request = new BidangUrusanRequest(namaBidangUrusan);
        BidangUrusanDto towerData = new BidangUrusanDto(10L, "BU-001", namaBidangUrusan);
        BidangUrusan savedBidangUrusan = new BidangUrusan(1L, kodeOpd, towerData.kodeBidangUrusan(), towerData.namaBidangUrusan(), Instant.now(), Instant.now());

        mockTowerDataResponse(Mono.just(List.of(towerData)));
        when(bidangUrusanRepository.existsByKodeOpdAndKodeBidangUrusan(kodeOpd, towerData.kodeBidangUrusan())).thenReturn(false);
        when(bidangUrusanRepository.save(any(BidangUrusan.class))).thenReturn(savedBidangUrusan);

        BidangUrusan result = bidangUrusanService.simpanBidangUrusan(kodeOpd, request);

        assertEquals(savedBidangUrusan, result);

        ArgumentCaptor<BidangUrusan> bidangUrusanCaptor = ArgumentCaptor.forClass(BidangUrusan.class);
        verify(bidangUrusanRepository).save(bidangUrusanCaptor.capture());
        BidangUrusan bidangUrusanToSave = bidangUrusanCaptor.getValue();
        assertEquals(kodeOpd, bidangUrusanToSave.kodeOpd());
        assertEquals(towerData.kodeBidangUrusan(), bidangUrusanToSave.kodeBidangUrusan());
        assertEquals(towerData.namaBidangUrusan(), bidangUrusanToSave.namaBidangUrusan());

        verify(bidangUrusanRepository).existsByKodeOpdAndKodeBidangUrusan(kodeOpd, towerData.kodeBidangUrusan());
    }

    @Test
    void simpanBidangUrusan_usesTowerKodeBidangUrusan_withCaseInsensitiveNama() {
        String kodeOpd = "OPD-02";
        BidangUrusanRequest request = new BidangUrusanRequest("bidang infrastruktur");
        BidangUrusanDto towerData = new BidangUrusanDto(20L, "BU-020", "Bidang Infrastruktur");
        BidangUrusan savedBidangUrusan = new BidangUrusan(2L, kodeOpd, towerData.kodeBidangUrusan(), towerData.namaBidangUrusan(), Instant.now(), Instant.now());

        mockTowerDataResponse(Mono.just(List.of(towerData)));
        when(bidangUrusanRepository.existsByKodeOpdAndKodeBidangUrusan(kodeOpd, towerData.kodeBidangUrusan())).thenReturn(false);
        when(bidangUrusanRepository.save(any(BidangUrusan.class))).thenReturn(savedBidangUrusan);

        BidangUrusan result = bidangUrusanService.simpanBidangUrusan(kodeOpd, request);

        assertEquals(towerData.kodeBidangUrusan(), result.kodeBidangUrusan());
        verify(bidangUrusanRepository).existsByKodeOpdAndKodeBidangUrusan(kodeOpd, towerData.kodeBidangUrusan());
    }

    @Test
    void simpanBidangUrusan_throwsException_whenTowerDataNotFound() {
        String kodeOpd = "OPD-01";
        BidangUrusanRequest request = new BidangUrusanRequest("Bidang Tidak Ada");
        mockTowerDataResponse(Mono.just(List.of()));

        assertThrows(BidangUrusanNotFoundException.class, () -> bidangUrusanService.simpanBidangUrusan(kodeOpd, request));

        verify(bidangUrusanRepository, never()).save(any());
    }

    @Test
    void simpanBidangUrusan_throwsException_whenBidangUrusanAlreadyExists() {
        String kodeOpd = "OPD-01";
        String namaBidangUrusan = "Bidang Infrastruktur";
        BidangUrusanRequest request = new BidangUrusanRequest(namaBidangUrusan);
        BidangUrusanDto towerData = new BidangUrusanDto(10L, "BU-001", namaBidangUrusan);

        mockTowerDataResponse(Mono.just(List.of(towerData)));
        when(bidangUrusanRepository.existsByKodeOpdAndKodeBidangUrusan(kodeOpd, towerData.kodeBidangUrusan())).thenReturn(true);

        assertThrows(BidangUrusanAlreadyExistException.class, () -> bidangUrusanService.simpanBidangUrusan(kodeOpd, request));

        verify(bidangUrusanRepository, never()).save(any());
    }

    @Test
    void hapusBidangUrusan_throwsForbidden_whenProgramExist() {
        String kodeBidangUrusan = "BU-001";
        BidangUrusan bidangUrusan = new BidangUrusan(1L, "OPD-01", kodeBidangUrusan, "Bidang Infrastruktur", Instant.now(), Instant.now());

        when(bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)).thenReturn(java.util.Optional.of(bidangUrusan));
        when(programRepository.existsByBidangUrusanId(bidangUrusan.id())).thenReturn(true);

        assertThrows(BidangUrusanDeleteForbiddenException.class, () -> bidangUrusanService.hapusBidangUrusan(kodeBidangUrusan));

        verify(bidangUrusanRepository, never()).deleteById(any());
    }

    @Test
    void hapusBidangUrusan_deletes_whenNoChildProgram() {
        String kodeBidangUrusan = "BU-001";
        BidangUrusan bidangUrusan = new BidangUrusan(1L, "OPD-01", kodeBidangUrusan, "Bidang Infrastruktur", Instant.now(), Instant.now());

        when(bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)).thenReturn(java.util.Optional.of(bidangUrusan));
        when(programRepository.existsByBidangUrusanId(bidangUrusan.id())).thenReturn(false);

        bidangUrusanService.hapusBidangUrusan(kodeBidangUrusan);

        verify(bidangUrusanRepository).deleteById(bidangUrusan.id());
    }

    @SuppressWarnings("unchecked")
    private void mockTowerDataResponse(Mono<List<BidangUrusanDto>> response) {
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(response);
    }
}
