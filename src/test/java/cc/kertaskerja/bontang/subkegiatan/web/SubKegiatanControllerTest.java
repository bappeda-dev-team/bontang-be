package cc.kertaskerja.bontang.subkegiatan.web;

import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatan;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatanService;
import cc.kertaskerja.bontang.subkegiatan.web.request.SubKegiatanBatchRequest;
import cc.kertaskerja.bontang.subkegiatan.web.request.SubKegiatanRequest;
import cc.kertaskerja.bontang.subkegiatan.web.response.SubKegiatanResponse;
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
    void getByKodeSubKegiatan_returnsSubKegiatanFromService() {
        String kodeSubKegiatan = "SK-001";
        Instant createdDate = Instant.parse("2024-01-01T00:00:00Z");
        Instant lastModifiedDate = Instant.parse("2024-01-02T00:00:00Z");
        SubKegiatan subKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1", createdDate, lastModifiedDate);

        when(subKegiatanService.detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(subKegiatan);

        SubKegiatan result = subKegiatanController.getByKodeSubKegiatan(kodeSubKegiatan);

        assertEquals(subKegiatan, result);
        verify(subKegiatanService).detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan);
    }

    @Test
    void findBatchByKodeSubKegiatan_returnsSubKegiatanListFromService() {
        List<String> kodeSubKegiatanList = List.of("SK-001", "SK-002");
        List<SubKegiatan> subKegiatanList = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z")),
                new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", Instant.parse("2024-02-01T00:00:00Z"), Instant.parse("2024-02-02T00:00:00Z"))
        );

        when(subKegiatanService.detailSubKegiatanIn(kodeSubKegiatanList)).thenReturn(subKegiatanList);

        List<SubKegiatan> result = subKegiatanController.findBatchByKodeSubKegiatan(new SubKegiatanBatchRequest(kodeSubKegiatanList));

        assertEquals(subKegiatanList, result);
        verify(subKegiatanService).detailSubKegiatanIn(kodeSubKegiatanList);
    }

    @Test
    void findAll_returnsSubKegiatanResponseListFromService() {
        List<SubKegiatan> subKegiatanList = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z")),
                new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", Instant.parse("2024-02-01T00:00:00Z"), Instant.parse("2024-02-02T00:00:00Z"))
        );

        when(subKegiatanService.findAll()).thenReturn(subKegiatanList);

        List<SubKegiatanResponse> result = subKegiatanController.findAll();

        assertEquals(subKegiatanList.size(), result.size());

        for (int i = 0; i < subKegiatanList.size(); i++) {
            SubKegiatan expected = subKegiatanList.get(i);
            SubKegiatanResponse response = result.get(i);

            assertEquals(expected.id(), response.id());
            assertEquals(expected.kodeSubKegiatan(), response.kodeSubKegiatan());
            assertEquals(expected.namaSubKegiatan(), response.namaSubKegiatan());
            assertEquals(expected.createdDate(), response.createdDate());
            assertEquals(expected.lastModifiedDate(), response.lastModifiedDate());
        }

        verify(subKegiatanService).findAll();
    }

    @Test
    void put_updatesSubKegiatanUsingService() {
        String kodeSubKegiatan = "SK-001";
        Instant createdDate = Instant.parse("2024-01-01T00:00:00Z");
        Instant lastModifiedDate = Instant.parse("2024-01-02T00:00:00Z");
        SubKegiatan existingSubKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1", createdDate, lastModifiedDate);
        SubKegiatanRequest request = new SubKegiatanRequest("SK-002", "Sub Kegiatan Updated");
        SubKegiatan updatedSubKegiatan = new SubKegiatan(1L, request.kodeSubKegiatan(), request.namaSubKegiatan(), createdDate, Instant.parse("2024-01-03T00:00:00Z"));

        when(subKegiatanService.detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(existingSubKegiatan);
        when(subKegiatanService.ubahSubKegiatan(eq(kodeSubKegiatan), any(SubKegiatan.class))).thenReturn(updatedSubKegiatan);

        SubKegiatan result = subKegiatanController.put(kodeSubKegiatan, request);

        ArgumentCaptor<SubKegiatan> subKegiatanCaptor = ArgumentCaptor.forClass(SubKegiatan.class);
        verify(subKegiatanService).detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan);
        verify(subKegiatanService).ubahSubKegiatan(eq(kodeSubKegiatan), subKegiatanCaptor.capture());

        SubKegiatan subKegiatanPassed = subKegiatanCaptor.getValue();
        assertEquals(existingSubKegiatan.id(), subKegiatanPassed.id());
        assertEquals(request.kodeSubKegiatan(), subKegiatanPassed.kodeSubKegiatan());
        assertEquals(request.namaSubKegiatan(), subKegiatanPassed.namaSubKegiatan());
        assertEquals(existingSubKegiatan.createdDate(), subKegiatanPassed.createdDate());
        assertNull(subKegiatanPassed.lastModifiedDate());

        assertEquals(updatedSubKegiatan, result);
    }

    @Test
    void post_createsSubKegiatanAndReturnsCreatedResponse() {
        SubKegiatanRequest request = new SubKegiatanRequest("SK-001", "Sub Kegiatan 1");
        SubKegiatan savedSubKegiatan = new SubKegiatan(1L, request.kodeSubKegiatan(), request.namaSubKegiatan(), Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(subKegiatanService.tambahSubKegiatan(any(SubKegiatan.class))).thenReturn(savedSubKegiatan);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/subkegiatan");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<SubKegiatan> response = subKegiatanController.post(request);

        ArgumentCaptor<SubKegiatan> subKegiatanCaptor = ArgumentCaptor.forClass(SubKegiatan.class);
        verify(subKegiatanService).tambahSubKegiatan(subKegiatanCaptor.capture());

        SubKegiatan subKegiatanPassed = subKegiatanCaptor.getValue();
        assertNull(subKegiatanPassed.id());
        assertEquals(request.kodeSubKegiatan(), subKegiatanPassed.kodeSubKegiatan());
        assertEquals(request.namaSubKegiatan(), subKegiatanPassed.namaSubKegiatan());
        assertNull(subKegiatanPassed.createdDate());
        assertNull(subKegiatanPassed.lastModifiedDate());

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
