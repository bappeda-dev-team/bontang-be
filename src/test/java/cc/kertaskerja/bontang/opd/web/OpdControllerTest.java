package cc.kertaskerja.bontang.opd.web;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanDto;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanService;
import cc.kertaskerja.bontang.bidangurusan.web.BidangUrusanRequest;
import cc.kertaskerja.bontang.opd.domain.Opd;
import cc.kertaskerja.bontang.opd.domain.OpdService;
import cc.kertaskerja.bontang.opd.domain.exception.OpdDeleteForbiddenException;
import cc.kertaskerja.bontang.opd.web.OpdBidangUrusanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class OpdControllerTest {

    @Mock
    private OpdService opdService;

    @Mock
    private BidangUrusanService bidangUrusanService;

    private OpdController opdController;

    @BeforeEach
    void setUp() {
        opdController = new OpdController(opdService, bidangUrusanService);
    }

    @Test
    void findAll_returnsAllOpdFromService() {
        Instant created = Instant.parse("2024-01-01T00:00:00Z");
        Instant modified = Instant.parse("2024-01-02T00:00:00Z");
        Iterable<Opd> opdList = java.util.List.of(
                new Opd(1L, "OPD-001", "BAPPEDA", created, modified),
                new Opd(2L, "OPD-002", "BPKAD", created, modified)
        );

        when(opdService.findAll()).thenReturn(opdList);
        when(bidangUrusanService.findByKodeOpd("OPD-001")).thenReturn(List.of(
                new BidangUrusan(1L, "OPD-001", "BID-01", "Urusan A", created, modified)
        ));
        when(bidangUrusanService.findByKodeOpd("OPD-002")).thenReturn(List.of());

        List<OpdResponse> result = opdController.findAll();

        List<OpdResponse> expected = List.of(
                new OpdResponse(1L, "OPD-001", "BAPPEDA", created, modified, List.of(
                        new OpdBidangUrusanResponse(1L, "BID-01", "Urusan A")
                )),
                new OpdResponse(2L, "OPD-002", "BPKAD", created, modified, List.of())
        );

        assertEquals(expected, result);
        verify(opdService).findAll();
        verify(bidangUrusanService, times(1)).findByKodeOpd("OPD-001");
        verify(bidangUrusanService, times(1)).findByKodeOpd("OPD-002");
    }

    @Test
    void getByKodeOpd_returnsOpdFromService() {
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());

        when(opdService.detailOpdByKodeOpd(kodeOpd)).thenReturn(opd);

        Opd result = opdController.getByKodeOpd(kodeOpd);

        assertEquals(opd, result);
        verify(opdService).detailOpdByKodeOpd(kodeOpd);
    }

    @Test
    void bidangUrusanFindAll_returnsNamaBidangUrusan() {
        List<BidangUrusanDto> options = java.util.List.of(
                new BidangUrusanDto(1L, "BID-01", "Urusan A"),
                new BidangUrusanDto(2L, "BID-02", "Urusan B")
        );

        when(bidangUrusanService.findAll()).thenReturn(options);

        Iterable<String> result = opdController.bidangUrusanFindAll();

        assertEquals(java.util.List.of("Urusan A", "Urusan B"), result);
        verify(bidangUrusanService).findAll();
        verifyNoInteractions(opdService);
    }

    @Test
    void put_updatesOpdUsingService() {
        String kodeOpd = "OPD-001";
        Opd existingOpd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));
        OpdRequest request = new OpdRequest(
                null,
                "OPD-002",
                "BAPPEDA UPDATED",
                List.of(new OpdBidangUrusanRequest(5L, "BID-01", "Bidang Diperbarui"))
        );

        Instant updatedAt = Instant.parse("2024-01-03T00:00:00Z");
        Opd updatedOpd = new Opd(1L, request.kodeOpd(), request.namaOpd(), existingOpd.createdDate(), updatedAt);

        when(opdService.detailOpdByKodeOpd(kodeOpd)).thenReturn(existingOpd);
        when(opdService.ubahOpd(eq(kodeOpd), any(Opd.class))).thenReturn(updatedOpd);
        when(bidangUrusanService.simpanAtauPerbaruiBidangUrusan(
                eq(kodeOpd),
                eq(request.kodeOpd()),
                eq(request.bidangUrusan())
        )).thenReturn(List.of(
                new BidangUrusan(5L, request.kodeOpd(), "BID-01", "Bidang Diperbarui", existingOpd.createdDate(), updatedAt)
        ));
        when(bidangUrusanService.findByKodeOpd(request.kodeOpd())).thenReturn(List.of(
                new BidangUrusan(5L, request.kodeOpd(), "BID-01", "Bidang Diperbarui", existingOpd.createdDate(), updatedAt)
        ));

        OpdResponse result = opdController.put(kodeOpd, request);

        ArgumentCaptor<Opd> opdCaptor = ArgumentCaptor.forClass(Opd.class);
        verify(opdService).detailOpdByKodeOpd(kodeOpd);
        verify(opdService).ubahOpd(eq(kodeOpd), opdCaptor.capture());
        verify(bidangUrusanService).simpanAtauPerbaruiBidangUrusan(kodeOpd, request.kodeOpd(), request.bidangUrusan());
        verify(bidangUrusanService).findByKodeOpd(request.kodeOpd());

        Opd opdPassed = opdCaptor.getValue();
        assertEquals(existingOpd.id(), opdPassed.id());
        assertEquals(request.kodeOpd(), opdPassed.kodeOpd());
        assertEquals(request.namaOpd(), opdPassed.namaOpd());
        assertEquals(existingOpd.createdDate(), opdPassed.createdDate());
        assertNull(opdPassed.lastModifiedDate());

        OpdResponse expected = new OpdResponse(
                updatedOpd.id(),
                updatedOpd.kodeOpd(),
                updatedOpd.namaOpd(),
                updatedOpd.createdDate(),
                updatedOpd.lastModifiedDate(),
                List.of(new OpdBidangUrusanResponse(5L, "BID-01", "Bidang Diperbarui"))
        );

        assertEquals(expected, result);
    }

    @Test
    void post_createsOpdAndReturnsCreatedResponse() {
        OpdRequest request = new OpdRequest(null, "OPD-001", "BAPPEDA", List.of());
        Opd savedOpd = new Opd(1L, request.kodeOpd(), request.namaOpd(), Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(opdService.tambahOpd(any(Opd.class))).thenReturn(savedOpd);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/opd");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<Opd> response = opdController.post(request);

        ArgumentCaptor<Opd> opdCaptor = ArgumentCaptor.forClass(Opd.class);
        verify(opdService).tambahOpd(opdCaptor.capture());

        Opd opdPassed = opdCaptor.getValue();
        assertEquals(request.kodeOpd(), opdPassed.kodeOpd());
        assertEquals(request.namaOpd(), opdPassed.namaOpd());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedOpd, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/opd/" + savedOpd.id(), location != null ? location.getPath() : null);
    }

    @Test
    void bidangUrusanSaved_returnsNamaBidangUrusanYangSudahDisimpan() {
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());
        Iterable<BidangUrusan> saved = List.of(
                new BidangUrusan(1L, kodeOpd, "BID-01", "Urusan A", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z")),
                new BidangUrusan(2L, kodeOpd, "BID-02", "Urusan B", Instant.parse("2024-02-01T00:00:00Z"), Instant.parse("2024-02-02T00:00:00Z"))
        );

        when(opdService.detailOpdByKodeOpd(kodeOpd)).thenReturn(opd);
        when(bidangUrusanService.findByKodeOpd(kodeOpd)).thenReturn(saved);

        Iterable<String> result = opdController.bidangUrusanSaved(kodeOpd);

        assertEquals(List.of("Urusan A", "Urusan B"), result);
        verify(opdService).detailOpdByKodeOpd(kodeOpd);
        verify(bidangUrusanService).findByKodeOpd(kodeOpd);
    }

    @Test
    void pilihBidangUrusan_menambahkanBidangUrusanKeOpd() {
        String kodeOpd = "OPD-001";
        BidangUrusanRequest request = new BidangUrusanRequest("Urusan A");
        Opd opd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());
        BidangUrusan saved = new BidangUrusan(10L, kodeOpd, "BID-01", request.namaBidangUrusan(), Instant.parse("2024-03-01T00:00:00Z"), Instant.parse("2024-03-02T00:00:00Z"));

        when(opdService.detailOpdByKodeOpd(kodeOpd)).thenReturn(opd);
        when(bidangUrusanService.simpanBidangUrusan(kodeOpd, request)).thenReturn(saved);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/opd/" + kodeOpd + "/bidang-urusan");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<BidangUrusan> response = opdController.pilihBidangUrusan(kodeOpd, request);

        verify(opdService).detailOpdByKodeOpd(kodeOpd);
        verify(bidangUrusanService).simpanBidangUrusan(kodeOpd, request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(saved, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/opd/" + kodeOpd + "/bidang-urusan/" + saved.id(), location != null ? location.getPath() : null);
    }

    @Test
    void delete_deletesOpdUsingService() {
        String kodeOpd = "OPD-001";

        opdController.delete(kodeOpd);

        verify(opdService).hapusOpd(kodeOpd);
    }

    @Test
    void delete_throwsDeleteForbidden_whenServiceMenolak() {
        String kodeOpd = "OPD-001";
        OpdDeleteForbiddenException ex = new OpdDeleteForbiddenException(kodeOpd);
        org.mockito.Mockito.doThrow(ex).when(opdService).hapusOpd(kodeOpd);

        assertThrows(OpdDeleteForbiddenException.class, () -> opdController.delete(kodeOpd));
        verify(opdService).hapusOpd(kodeOpd);
    }
}
