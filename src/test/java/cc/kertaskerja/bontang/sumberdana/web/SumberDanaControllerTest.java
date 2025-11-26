package cc.kertaskerja.bontang.sumberdana.web;

import cc.kertaskerja.bontang.sumberdana.domain.SetInput;
import cc.kertaskerja.bontang.sumberdana.domain.SumberDana;
import cc.kertaskerja.bontang.sumberdana.domain.SumberDanaService;
import org.junit.jupiter.api.AfterEach;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SumberDanaControllerTest {

    @Mock
    private SumberDanaService sumberDanaService;

    private SumberDanaController sumberDanaController;

    @BeforeEach
    void setUp() {
        sumberDanaController = new SumberDanaController(sumberDanaService);
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void getById_returnsSumberDanaFromService() {
        Long id = 1L;
        SumberDana sumberDana = new SumberDana(id, "KD-001", "Dana Pendidikan", "KD-101", SetInput.Ya, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));

        when(sumberDanaService.detailSumberDanaById(id)).thenReturn(sumberDana);

        SumberDana result = sumberDanaController.getById(id);

        assertEquals(sumberDana, result);
        verify(sumberDanaService).detailSumberDanaById(id);
    }

    @Test
    void findAll_returnsAllSumberDanaFromService() {
        Iterable<SumberDana> sumberDanaList = List.of(
                new SumberDana(1L, "KD-001", "Dana Pendidikan", "KD-101", SetInput.Ya, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z")),
                new SumberDana(2L, "KD-002", "Dana Kesehatan", "KD-102", SetInput.Tidak, Instant.parse("2024-02-01T00:00:00Z"), Instant.parse("2024-02-02T00:00:00Z"))
        );

        when(sumberDanaService.findAll()).thenReturn(sumberDanaList);

        Iterable<SumberDana> result = sumberDanaController.findAll();

        assertEquals(sumberDanaList, result);
        verify(sumberDanaService).findAll();
    }

    @Test
    void put_updatesSumberDanaUsingService() {
        Long id = 1L;
        Instant createdDate = Instant.parse("2024-01-01T00:00:00Z");
        SumberDana existingSumberDana = new SumberDana(id, "KD-001", "Dana Pendidikan", "KD-101", SetInput.Ya, createdDate, Instant.parse("2024-01-02T00:00:00Z"));
        SumberDanaRequest request = new SumberDanaRequest(null, "KD-010", "Dana Pendidikan Updated", "KD-110", SetInput.Tidak);

        SumberDana updatedSumberDana = new SumberDana(id, request.kodeDanaLama(), request.sumberDana(), request.kodeDanaBaru(), request.setInput(), createdDate, Instant.parse("2024-01-03T00:00:00Z"));

        when(sumberDanaService.detailSumberDanaById(id)).thenReturn(existingSumberDana);
        when(sumberDanaService.ubahSumberDana(eq(id), any(SumberDana.class))).thenReturn(updatedSumberDana);

        SumberDana result = sumberDanaController.put(id, request);

        ArgumentCaptor<SumberDana> sumberDanaCaptor = ArgumentCaptor.forClass(SumberDana.class);
        verify(sumberDanaService).detailSumberDanaById(id);
        verify(sumberDanaService).ubahSumberDana(eq(id), sumberDanaCaptor.capture());

        SumberDana sumberDanaPassed = sumberDanaCaptor.getValue();
        assertEquals(existingSumberDana.id(), sumberDanaPassed.id());
        assertEquals(request.kodeDanaLama(), sumberDanaPassed.kodeDanaLama());
        assertEquals(request.sumberDana(), sumberDanaPassed.sumberDana());
        assertEquals(request.kodeDanaBaru(), sumberDanaPassed.kodeDanaBaru());
        assertEquals(request.setInput(), sumberDanaPassed.setInput());
        assertEquals(existingSumberDana.createdDate(), sumberDanaPassed.createdDate());
        assertNull(sumberDanaPassed.lastModifiedDate());

        assertEquals(updatedSumberDana, result);
    }

    @Test
    void post_createsSumberDanaAndReturnsCreatedResponse() {
        SumberDanaRequest request = new SumberDanaRequest(null, "KD-001", "Dana Pendidikan", "KD-101", SetInput.Ya);
        SumberDana savedSumberDana = new SumberDana(10L, request.kodeDanaLama(), request.sumberDana(), request.kodeDanaBaru(), request.setInput(), Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));

        when(sumberDanaService.tambahSumberDana(any(SumberDana.class))).thenReturn(savedSumberDana);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/sumberdana");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<SumberDana> response = sumberDanaController.post(request);

        ArgumentCaptor<SumberDana> sumberDanaCaptor = ArgumentCaptor.forClass(SumberDana.class);
        verify(sumberDanaService).tambahSumberDana(sumberDanaCaptor.capture());

        SumberDana sumberDanaPassed = sumberDanaCaptor.getValue();
        assertEquals(request.kodeDanaLama(), sumberDanaPassed.kodeDanaLama());
        assertEquals(request.sumberDana(), sumberDanaPassed.sumberDana());
        assertEquals(request.kodeDanaBaru(), sumberDanaPassed.kodeDanaBaru());
        assertEquals(request.setInput(), sumberDanaPassed.setInput());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedSumberDana, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertEquals("/sumberdana/" + savedSumberDana.id(), location.getPath());
    }

    @Test
    void delete_deletesSumberDanaUsingService() {
        Long id = 1L;

        sumberDanaController.delete(id);

        verify(sumberDanaService).hapusSumberDana(id);
    }
}
