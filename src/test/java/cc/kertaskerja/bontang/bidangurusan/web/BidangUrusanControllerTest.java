package cc.kertaskerja.bontang.bidangurusan.web;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanDto;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.ArgumentMatchers.eq;
import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidangUrusanControllerTest {

    @Mock
    private BidangUrusanService bidangUrusanService;

    private BidangUrusanController bidangUrusanController;

    @BeforeEach
    void setUp() {
        bidangUrusanController = new BidangUrusanController(bidangUrusanService);
    }

    @Test
    void findAll_returnsDataFromService() {
        List<BidangUrusanDto> data = List.of(
                new BidangUrusanDto(1L, "BU-001", "Bidang Infrastruktur"),
                new BidangUrusanDto(2L, "BU-002", "Bidang Pelayanan")
        );

        when(bidangUrusanService.findAll()).thenReturn(data);

        List<BidangUrusanDto> result = bidangUrusanController.findAll();

        assertEquals(data, result);
        verify(bidangUrusanService).findAll();
    }

    @Test
    void findByKodeOpd_returnsSavedBidangUrusan() {
        String kodeOpd = "OPD-01";
        Iterable<BidangUrusan> saved = List.of(
                new BidangUrusan(1L, kodeOpd, "BU-001", "Bidang Infrastruktur", Instant.now(), Instant.now()),
                new BidangUrusan(2L, kodeOpd, "BU-002", "Bidang Pelayanan", Instant.now(), Instant.now())
        );

        when(bidangUrusanService.findByKodeOpd(kodeOpd)).thenReturn(saved);

        Iterable<BidangUrusan> result = bidangUrusanController.findByKodeOpd(kodeOpd);

        assertEquals(saved, result);
        verify(bidangUrusanService).findByKodeOpd(kodeOpd);
    }

    @Test
    void post_menyimpanBidangUrusanDanReturnCreatedResponse() {
        String kodeOpd = "OPD-01";
        BidangUrusanRequest request = new BidangUrusanRequest("Bidang Infrastruktur");
        BidangUrusan saved = new BidangUrusan(10L, kodeOpd, "BU-001", request.namaBidangUrusan(), Instant.now(), Instant.now());

        when(bidangUrusanService.simpanBidangUrusan(kodeOpd, request)).thenReturn(saved);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/bidangurusan/" + kodeOpd);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<BidangUrusan> response = bidangUrusanController.post(kodeOpd, request);

        ArgumentCaptor<BidangUrusanRequest> requestCaptor = ArgumentCaptor.forClass(BidangUrusanRequest.class);
        verify(bidangUrusanService).simpanBidangUrusan(eq(kodeOpd), requestCaptor.capture());
        assertEquals(request.namaBidangUrusan(), requestCaptor.getValue().namaBidangUrusan());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(saved, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/bidangurusan/" + kodeOpd + "/" + saved.id(), location != null ? location.getPath() : null);
    }

    @Test
    void delete_menghapusBidangUrusan() {
        String kodeBidangUrusan = "BU-001";

        bidangUrusanController.delete(kodeBidangUrusan);

        verify(bidangUrusanService).hapusBidangUrusan(kodeBidangUrusan);
    }
}
