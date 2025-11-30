package cc.kertaskerja.bontang.subkegiatan.web;

import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatan;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatanService;
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
public class SubKegiatanControllerTest {

    @Mock
    private SubKegiatanService subKegiatanService;

    private SubKegiatanController subKegiatanController;

    @BeforeEach
    void setUp() {
        subKegiatanController = new SubKegiatanController(subKegiatanService);
    }

    @Test
    void findAll_returnsAllSubKegiatanFromService() {
        Iterable<SubKegiatan> subKegiatanList = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", 10L, Instant.now(), Instant.now()),
                new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", 20L, Instant.now(), Instant.now())
        );

        when(subKegiatanService.findAll()).thenReturn(subKegiatanList);

        Iterable<SubKegiatan> result = subKegiatanController.findAll();

        assertEquals(subKegiatanList, result);
        verify(subKegiatanService).findAll();
    }

    @Test
    void getByKodeSubKegiatan_returnsSubKegiatanFromService() {
        String kodeSubKegiatan = "SK-001";
        SubKegiatan subKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1", 10L, Instant.now(), Instant.now());

        when(subKegiatanService.detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(subKegiatan);

        SubKegiatan result = subKegiatanController.getByKodeSubKegiatan(kodeSubKegiatan);

        assertEquals(subKegiatan, result);
        verify(subKegiatanService).detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan);
    }

    @Test
    void findBatch_returnsSubKegiatanListFromService() {
        List<String> kodeSubKegiatanList = List.of("SK-001", "SK-002");
        List<SubKegiatan> subKegiatanList = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", 10L, Instant.now(), Instant.now()),
                new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", 20L, Instant.now(), Instant.now())
        );

        when(subKegiatanService.detailSubKegiatanByKodeSubKegiatanIn(kodeSubKegiatanList)).thenReturn(subKegiatanList);

        List<SubKegiatan> result = subKegiatanController.findBatch(new SubKegiatanBatchRequest(kodeSubKegiatanList));

        assertEquals(subKegiatanList, result);
        verify(subKegiatanService).detailSubKegiatanByKodeSubKegiatanIn(kodeSubKegiatanList);
    }

    @Test
    void put_updatesSubKegiatanUsingService() {
        String kodeSubKegiatan = "SK-001";
        String kodeKegiatan = "KG-01";
        SubKegiatan existingSubKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1", 30L, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));
        SubKegiatanRequest request = new SubKegiatanRequest(null, "SK-002", "Sub Kegiatan Updated", kodeKegiatan);
        SubKegiatan updatedSubKegiatan = new SubKegiatan(1L, request.kodeSubKegiatan(), request.namaSubKegiatan(), 40L, existingSubKegiatan.createdDate(), Instant.parse("2024-01-03T00:00:00Z"));

        when(subKegiatanService.detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(existingSubKegiatan);
        when(subKegiatanService.ubahSubKegiatan(eq(kodeSubKegiatan), any(SubKegiatan.class), eq(kodeKegiatan))).thenReturn(updatedSubKegiatan);

        SubKegiatan result = subKegiatanController.put(kodeSubKegiatan, request);

        ArgumentCaptor<SubKegiatan> subKegiatanCaptor = ArgumentCaptor.forClass(SubKegiatan.class);
        verify(subKegiatanService).detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan);
        verify(subKegiatanService).ubahSubKegiatan(eq(kodeSubKegiatan), subKegiatanCaptor.capture(), eq(kodeKegiatan));

        SubKegiatan subKegiatanPassed = subKegiatanCaptor.getValue();
        assertEquals(existingSubKegiatan.id(), subKegiatanPassed.id());
        assertEquals(request.kodeSubKegiatan(), subKegiatanPassed.kodeSubKegiatan());
        assertEquals(request.namaSubKegiatan(), subKegiatanPassed.namaSubKegiatan());
        assertEquals(existingSubKegiatan.kegiatanId(), subKegiatanPassed.kegiatanId());
        assertEquals(existingSubKegiatan.createdDate(), subKegiatanPassed.createdDate());
        assertNull(subKegiatanPassed.lastModifiedDate());

        assertEquals(updatedSubKegiatan, result);
    }

    @Test
    void post_createsSubKegiatanAndReturnsCreatedResponse() {
        String kodeKegiatan = "KG-01";
        SubKegiatanRequest request = new SubKegiatanRequest(null, "SK-001", "Sub Kegiatan 1", kodeKegiatan);
        SubKegiatan savedSubKegiatan = new SubKegiatan(1L, request.kodeSubKegiatan(), request.namaSubKegiatan(), 10L, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(subKegiatanService.tambahSubKegiatan(any(SubKegiatan.class), eq(kodeKegiatan))).thenReturn(savedSubKegiatan);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/subkegiatan");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<SubKegiatan> response = subKegiatanController.post(request);

        ArgumentCaptor<SubKegiatan> subKegiatanCaptor = ArgumentCaptor.forClass(SubKegiatan.class);
        verify(subKegiatanService).tambahSubKegiatan(subKegiatanCaptor.capture(), eq(kodeKegiatan));

        SubKegiatan subKegiatanPassed = subKegiatanCaptor.getValue();
        assertNull(subKegiatanPassed.id());
        assertEquals(request.kodeSubKegiatan(), subKegiatanPassed.kodeSubKegiatan());
        assertEquals(request.namaSubKegiatan(), subKegiatanPassed.namaSubKegiatan());
        assertNull(subKegiatanPassed.kegiatanId());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedSubKegiatan, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/subkegiatan/" + savedSubKegiatan.id(), location != null ? location.getPath() : null);
    }

    @Test
    void delete_deletesSubKegiatanUsingService() {
        String kodeSubKegiatan = "SK-001";

        subKegiatanController.delete(kodeSubKegiatan);

        verify(subKegiatanService).hapusSubKegiatan(kodeSubKegiatan);
    }
}
