package cc.kertaskerja.bontang.kegiatan.web;

import cc.kertaskerja.bontang.kegiatan.domain.Kegiatan;
import cc.kertaskerja.bontang.kegiatan.domain.KegiatanService;
import cc.kertaskerja.bontang.kegiatan.web.request.KegiatanBatchRequest;
import cc.kertaskerja.bontang.kegiatan.web.request.KegiatanRequest;
import cc.kertaskerja.bontang.kegiatan.web.response.KegiatanResponse;

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
public class KegiatanControllerTest {

    @Mock
    private KegiatanService kegiatanService;

    private KegiatanController kegiatanController;

    @BeforeEach
    void setUp() {
        kegiatanController = new KegiatanController(kegiatanService);
    }

    @Test
    void findAll_returnsAllKegiatanFromService() {
        Instant created1 = Instant.parse("2024-01-01T00:00:00Z");
        Instant modified1 = Instant.parse("2024-01-02T00:00:00Z");
        Instant created2 = Instant.parse("2024-02-01T00:00:00Z");
        Instant modified2 = Instant.parse("2024-02-02T00:00:00Z");
        Iterable<Kegiatan> kegiatanList = List.of(
                new Kegiatan(1L, "KG-001", "Kegiatan 1", 10L, created1, modified1),
                new Kegiatan(2L, "KG-002", "Kegiatan 2", 11L, created2, modified2)
        );

        when(kegiatanService.findAll()).thenReturn(kegiatanList);
        when(kegiatanService.getKodeProgram(10L)).thenReturn("PR-10");
        when(kegiatanService.getKodeProgram(11L)).thenReturn("PR-11");

        List<KegiatanResponse> result = kegiatanController.findAll();

        List<KegiatanResponse> expected = List.of(
                new KegiatanResponse(1L, "KG-001", "Kegiatan 1", "PR-10", created1, modified1),
                new KegiatanResponse(2L, "KG-002", "Kegiatan 2", "PR-11", created2, modified2)
        );

        assertEquals(expected, result);
        verify(kegiatanService).findAll();
        verify(kegiatanService).getKodeProgram(10L);
        verify(kegiatanService).getKodeProgram(11L);
    }

    @Test
    void getByKodeKegiatan_returnsKegiatanFromService() {
        String kodeKegiatan = "KG-001";
        Kegiatan kegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1", 10L, Instant.now(), Instant.now());

        when(kegiatanService.detailKegiatanByKodeKegiatan(kodeKegiatan)).thenReturn(kegiatan);

        Kegiatan result = kegiatanController.getByKodeKegiatan(kodeKegiatan);

        assertEquals(kegiatan, result);
        verify(kegiatanService).detailKegiatanByKodeKegiatan(kodeKegiatan);
    }

    @Test
    void findBatch_returnsKegiatanFromService() {
        KegiatanBatchRequest request = new KegiatanBatchRequest(List.of("KG-001", "KG-002"));
        List<Kegiatan> kegiatans = List.of(
                new Kegiatan(1L, "KG-001", "Kegiatan 1", 10L, Instant.now(), Instant.now()),
                new Kegiatan(2L, "KG-002", "Kegiatan 2", 11L, Instant.now(), Instant.now())
        );

        when(kegiatanService.detailKegiatanByKodeKegiatanIn(request.kodeKegiatan())).thenReturn(kegiatans);

        List<Kegiatan> result = kegiatanController.findBatch(request);

        assertEquals(kegiatans, result);
        verify(kegiatanService).detailKegiatanByKodeKegiatanIn(request.kodeKegiatan());
    }

    @Test
    void put_updatesKegiatanUsingService() {
        String kodeKegiatan = "KG-001";
        String kodeProgram = "PR-01";
        Kegiatan existingKegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1", 20L, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));
        KegiatanRequest request = new KegiatanRequest(null, "KG-002", "Kegiatan Updated", kodeProgram);
        Kegiatan updatedKegiatan = new Kegiatan(1L, request.kodeKegiatan(), request.namaKegiatan(), 30L, existingKegiatan.createdDate(), Instant.parse("2024-01-03T00:00:00Z"));

        when(kegiatanService.detailKegiatanByKodeKegiatan(kodeKegiatan)).thenReturn(existingKegiatan);
        when(kegiatanService.ubahKegiatan(eq(kodeKegiatan), any(Kegiatan.class), eq(kodeProgram))).thenReturn(updatedKegiatan);

        Kegiatan result = kegiatanController.put(kodeKegiatan, request);

        ArgumentCaptor<Kegiatan> kegiatanCaptor = ArgumentCaptor.forClass(Kegiatan.class);
        verify(kegiatanService).detailKegiatanByKodeKegiatan(kodeKegiatan);
        verify(kegiatanService).ubahKegiatan(eq(kodeKegiatan), kegiatanCaptor.capture(), eq(kodeProgram));

        Kegiatan kegiatanPassed = kegiatanCaptor.getValue();
        assertEquals(existingKegiatan.id(), kegiatanPassed.id());
        assertEquals(request.kodeKegiatan(), kegiatanPassed.kodeKegiatan());
        assertEquals(request.namaKegiatan(), kegiatanPassed.namaKegiatan());
        assertEquals(existingKegiatan.programId(), kegiatanPassed.programId());
        assertEquals(existingKegiatan.createdDate(), kegiatanPassed.createdDate());
        assertNull(kegiatanPassed.lastModifiedDate());

        assertEquals(updatedKegiatan, result);
    }

    @Test
    void post_createsKegiatanAndReturnsCreatedResponse() {
        String kodeProgram = "PR-01";
        KegiatanRequest request = new KegiatanRequest(null, "KG-001", "Kegiatan 1", kodeProgram);
        Kegiatan savedKegiatan = new Kegiatan(1L, request.kodeKegiatan(), request.namaKegiatan(), 10L, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(kegiatanService.tambahKegiatan(any(Kegiatan.class), eq(kodeProgram))).thenReturn(savedKegiatan);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/kegiatan");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<Kegiatan> response = kegiatanController.post(request);

        ArgumentCaptor<Kegiatan> kegiatanCaptor = ArgumentCaptor.forClass(Kegiatan.class);
        verify(kegiatanService).tambahKegiatan(kegiatanCaptor.capture(), eq(kodeProgram));

        Kegiatan kegiatanPassed = kegiatanCaptor.getValue();
        assertNull(kegiatanPassed.id());
        assertEquals(request.kodeKegiatan(), kegiatanPassed.kodeKegiatan());
        assertEquals(request.namaKegiatan(), kegiatanPassed.namaKegiatan());
        assertNull(kegiatanPassed.programId());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedKegiatan, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/kegiatan/" + savedKegiatan.id(), location != null ? location.getPath() : null);
    }

    @Test
    void delete_deletesKegiatanUsingService() {
        String kodeKegiatan = "KG-001";

        kegiatanController.delete(kodeKegiatan);

        verify(kegiatanService).hapusKegiatan(kodeKegiatan);
    }
}
