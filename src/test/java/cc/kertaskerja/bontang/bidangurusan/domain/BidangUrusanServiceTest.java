package cc.kertaskerja.bontang.bidangurusan.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanAlreadyExistException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import cc.kertaskerja.bontang.bidangurusan.web.BidangUrusanRequest;
import cc.kertaskerja.bontang.opd.web.OpdBidangUrusanRequest;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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

    private BidangUrusanService bidangUrusanService;

    @BeforeEach
    void setUp() {
        bidangUrusanService = new BidangUrusanService(towerDataWebClient, bidangUrusanRepository);

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
    void simpanBidangUrusan_acceptsKodeBidangUrusan_asIdentifier() {
        String kodeOpd = "OPD-02";
        BidangUrusanRequest request = new BidangUrusanRequest("BU-020");
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
    void simpanAtauPerbaruiBidangUrusan_updatesExistingAndAddsNewData() {
        String existingKodeOpd = "OPD-01";
        String targetKodeOpd = "OPD-02";
        BidangUrusan existing = new BidangUrusan(5L, existingKodeOpd, "BU-001", "Bidang Lama", Instant.now(), Instant.now());
        OpdBidangUrusanRequest updated = new OpdBidangUrusanRequest(existing.id(), "BU-001", "Bidang Baru");
        OpdBidangUrusanRequest baru = new OpdBidangUrusanRequest(null, "BU-002", "Bidang Baru 2");

        when(bidangUrusanRepository.findByKodeOpd(existingKodeOpd)).thenReturn(List.of(existing));
        when(bidangUrusanRepository.findByKodeOpd(targetKodeOpd)).thenReturn(List.of());
        when(bidangUrusanRepository.existsByKodeOpdAndKodeBidangUrusan(targetKodeOpd, "BU-002")).thenReturn(false);

        AtomicLong sequence = new AtomicLong(10);
        when(bidangUrusanRepository.save(any(BidangUrusan.class))).thenAnswer(invocation -> {
            BidangUrusan arg = invocation.getArgument(0);
            Long id = arg.id() != null ? arg.id() : sequence.getAndIncrement();
            return new BidangUrusan(id, arg.kodeOpd(), arg.kodeBidangUrusan(), arg.namaBidangUrusan(), arg.createdDate(), arg.lastModifiedDate());
        });

        List<BidangUrusan> result = bidangUrusanService.simpanAtauPerbaruiBidangUrusan(existingKodeOpd, targetKodeOpd, List.of(updated, baru));

        assertEquals(2, result.size());
        verify(bidangUrusanRepository, times(2)).save(any(BidangUrusan.class));
        verify(bidangUrusanRepository, never()).deleteById(existing.id());
    }

    @Test
    void simpanAtauPerbaruiBidangUrusan_menghapusDataYangTidakLagiDipilih() {
        String kodeOpd = "OPD-01";
        BidangUrusan existing = new BidangUrusan(7L, kodeOpd, "BU-001", "Bidang Lama", Instant.now(), Instant.now());

        when(bidangUrusanRepository.findByKodeOpd(kodeOpd)).thenReturn(List.of(existing));

        bidangUrusanService.simpanAtauPerbaruiBidangUrusan(kodeOpd, kodeOpd, List.of());

        verify(bidangUrusanRepository).deleteById(existing.id());
    }

    @Test
    void hapusBidangUrusan_deletesWhenFound() {
        String kodeBidangUrusan = "BU-001";
        String kodeOpd = "OPD-01";
        BidangUrusan bidangUrusan = new BidangUrusan(1L, kodeOpd, kodeBidangUrusan, "Bidang Infrastruktur", Instant.now(), Instant.now());

        when(bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)).thenReturn(Optional.of(bidangUrusan));

        bidangUrusanService.hapusBidangUrusan(kodeBidangUrusan);

        verify(bidangUrusanRepository).findByKodeBidangUrusan(kodeBidangUrusan);
        verify(bidangUrusanRepository).deleteById(bidangUrusan.id());
    }

    @Test
    void hapusBidangUrusan_throwsException_whenNotFound() {
        String kodeBidangUrusan = "BU-999";

        when(bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)).thenReturn(Optional.empty());

        assertThrows(BidangUrusanNotFoundException.class, () -> bidangUrusanService.hapusBidangUrusan(kodeBidangUrusan));

        verify(bidangUrusanRepository, never()).deleteById(any());
    }

    @Test
    void hapusBidangUrusanByOpd_deletesWhenIdentifierMatchesKode() {
        String kodeOpd = "OPD-02";
        String identifier = "BU-010";
        BidangUrusan bidangUrusan = new BidangUrusan(3L, kodeOpd, identifier, "Bidang Infrastruktur", Instant.now(), Instant.now());

        when(bidangUrusanRepository.findByKodeOpdAndKodeBidangUrusan(kodeOpd, identifier)).thenReturn(Optional.of(bidangUrusan));

        bidangUrusanService.hapusBidangUrusan(kodeOpd, identifier);

        verify(bidangUrusanRepository).deleteById(bidangUrusan.id());
    }

    @Test
    void hapusBidangUrusanByOpd_resolvesIdentifierViaTowerData() {
        String kodeOpd = "OPD-02";
        String identifier = "Bidang Infrastruktur";
        BidangUrusanDto towerData = new BidangUrusanDto(15L, "BU-015", identifier);
        BidangUrusan bidangUrusan = new BidangUrusan(4L, kodeOpd, towerData.kodeBidangUrusan(), identifier, Instant.now(), Instant.now());

        mockTowerDataResponse(Mono.just(List.of(towerData)));
        when(bidangUrusanRepository.findByKodeOpdAndKodeBidangUrusan(kodeOpd, identifier)).thenReturn(Optional.empty());
        when(bidangUrusanRepository.findByKodeOpdAndKodeBidangUrusan(kodeOpd, towerData.kodeBidangUrusan())).thenReturn(Optional.of(bidangUrusan));

        bidangUrusanService.hapusBidangUrusan(kodeOpd, identifier);

        verify(bidangUrusanRepository).deleteById(bidangUrusan.id());
    }

    @Test
    void hapusBidangUrusanByOpd_throwsException_whenIdentifierNotFound() {
        String kodeOpd = "OPD-02";
        String identifier = "Tidak Ada";

        mockTowerDataResponse(Mono.just(List.of()));
        when(bidangUrusanRepository.findByKodeOpdAndKodeBidangUrusan(kodeOpd, identifier)).thenReturn(Optional.empty());

        assertThrows(BidangUrusanNotFoundException.class, () -> bidangUrusanService.hapusBidangUrusan(kodeOpd, identifier));

        verify(bidangUrusanRepository, never()).deleteById(any());
    }

    @Test
    void pindahBidangUrusanKeOpd_memindahkanSemuaBidang() {
        String sumber = "OPD-01";
        String target = "OPD-99";
        BidangUrusan bidang = new BidangUrusan(11L, sumber, "BU-001", "Bidang Lama", Instant.now(), Instant.now());

        when(bidangUrusanRepository.findByKodeOpd(sumber)).thenReturn(List.of(bidang));
        when(bidangUrusanRepository.findByKodeOpd(target)).thenReturn(List.of());

        bidangUrusanService.pindahBidangUrusanKeOpd(sumber, target);

        ArgumentCaptor<BidangUrusan> captor = ArgumentCaptor.forClass(BidangUrusan.class);
        verify(bidangUrusanRepository).save(captor.capture());
        assertEquals(target, captor.getValue().kodeOpd());
    }

    @SuppressWarnings("unchecked")
    private void mockTowerDataResponse(Mono<List<BidangUrusanDto>> response) {
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(response);
    }
}
