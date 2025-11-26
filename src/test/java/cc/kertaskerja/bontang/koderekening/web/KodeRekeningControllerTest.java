package cc.kertaskerja.bontang.koderekening.web;

import cc.kertaskerja.bontang.koderekening.domain.KodeRekening;
import cc.kertaskerja.bontang.koderekening.domain.KodeRekeningService;
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

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KodeRekeningControllerTest {

    @Mock
    private KodeRekeningService kodeRekeningService;

    private KodeRekeningController kodeRekeningController;

    @BeforeEach
    void setUp() {
        kodeRekeningController = new KodeRekeningController(kodeRekeningService);
    }

    @Test
    void findAll_returnsAllKodeRekeningFromService() {
        Iterable<KodeRekening> kodeRekenings = List.of(
                new KodeRekening(1L, "KR-001", "Rekening 1", Instant.now(), Instant.now()),
                new KodeRekening(2L, "KR-002", "Rekening 2", Instant.now(), Instant.now())
        );

        when(kodeRekeningService.findAll()).thenReturn(kodeRekenings);

        Iterable<KodeRekening> result = kodeRekeningController.findAll();

        assertEquals(kodeRekenings, result);
        verify(kodeRekeningService).findAll();
    }

    @Test
    void getByKodeRekening_returnsKodeRekeningFromService() {
        String kodeRekening = "KR-001";
        KodeRekening kodeRekeningData = new KodeRekening(1L, kodeRekening, "Rekening 1", Instant.now(), Instant.now());

        when(kodeRekeningService.detailKodeRekeningByKodeRekening(kodeRekening)).thenReturn(kodeRekeningData);

        KodeRekening result = kodeRekeningController.getByKodeRekening(kodeRekening);

        assertEquals(kodeRekeningData, result);
        verify(kodeRekeningService).detailKodeRekeningByKodeRekening(kodeRekening);
    }

    @Test
    void put_updatesKodeRekeningUsingService() {
        String kodeRekening = "KR-001";
        Instant createdDate = Instant.parse("2024-01-01T00:00:00Z");
        KodeRekening existingKodeRekening = new KodeRekening(1L, kodeRekening, "Rekening 1", createdDate, Instant.parse("2024-01-02T00:00:00Z"));
        KodeRekeningRequest request = new KodeRekeningRequest(null, "KR-002", "Rekening Updated");
        KodeRekening updatedKodeRekening = new KodeRekening(1L, request.kodeRekening(), request.namaRekening(), createdDate, Instant.parse("2024-01-03T00:00:00Z"));

        when(kodeRekeningService.detailKodeRekeningByKodeRekening(kodeRekening)).thenReturn(existingKodeRekening);
        when(kodeRekeningService.ubahKodeRekening(eq(kodeRekening), any(KodeRekening.class))).thenReturn(updatedKodeRekening);

        KodeRekening result = kodeRekeningController.put(kodeRekening, request);

        ArgumentCaptor<KodeRekening> kodeRekeningCaptor = ArgumentCaptor.forClass(KodeRekening.class);
        verify(kodeRekeningService).detailKodeRekeningByKodeRekening(kodeRekening);
        verify(kodeRekeningService).ubahKodeRekening(eq(kodeRekening), kodeRekeningCaptor.capture());

        KodeRekening kodeRekeningPassed = kodeRekeningCaptor.getValue();
        assertEquals(existingKodeRekening.id(), kodeRekeningPassed.id());
        assertEquals(request.kodeRekening(), kodeRekeningPassed.kodeRekening());
        assertEquals(request.namaRekening(), kodeRekeningPassed.namaRekening());
        assertEquals(existingKodeRekening.createdDate(), kodeRekeningPassed.createdDate());
        assertNull(kodeRekeningPassed.lastModifiedDate());

        assertEquals(updatedKodeRekening, result);
    }

    @Test
    void post_createsKodeRekeningAndReturnsCreatedResponse() {
        KodeRekeningRequest request = new KodeRekeningRequest(null, "KR-001", "Rekening 1");
        KodeRekening savedKodeRekening = new KodeRekening(1L, request.kodeRekening(), request.namaRekening(), Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(kodeRekeningService.tambahKodeRekening(any(KodeRekening.class))).thenReturn(savedKodeRekening);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/koderekening");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<KodeRekening> response = kodeRekeningController.post(request);

        ArgumentCaptor<KodeRekening> kodeRekeningCaptor = ArgumentCaptor.forClass(KodeRekening.class);
        verify(kodeRekeningService).tambahKodeRekening(kodeRekeningCaptor.capture());

        KodeRekening kodeRekeningPassed = kodeRekeningCaptor.getValue();
        assertNull(kodeRekeningPassed.id());
        assertEquals(request.kodeRekening(), kodeRekeningPassed.kodeRekening());
        assertEquals(request.namaRekening(), kodeRekeningPassed.namaRekening());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedKodeRekening, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/koderekening/" + savedKodeRekening.id(), location != null ? location.getPath() : null);
    }

    @Test
    void delete_deletesKodeRekeningUsingService() {
        String kodeRekening = "KR-001";

        kodeRekeningController.delete(kodeRekening);

        verify(kodeRekeningService).hapusKodeRekening(kodeRekening);
    }
}
