package cc.kertaskerja.bontang.rencanakinerja.web;

import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RencanaKinerjaControllerTest {

    @Mock
    private RencanaKinerjaService rencanaKinerjaService;

    private RencanaKinerjaController rencanaKinerjaController;

    @BeforeEach
    void setUp() {
        rencanaKinerjaController = new RencanaKinerjaController(rencanaKinerjaService);
    }

    @Test
    void getById_returnsEntityFromService() {
        Long id = 1L;
        RencanaKinerja expected = sampleRencanaKinerja(id);
        when(rencanaKinerjaService.detailRencanaKinerjaById(id)).thenReturn(expected);

        RencanaKinerja result = rencanaKinerjaController.getById(id);

        assertThat(result).isEqualTo(expected);
        verify(rencanaKinerjaService).detailRencanaKinerjaById(id);
    }

    @Test
    void findAll_returnsAllEntities() {
        List<RencanaKinerja> data = List.of(sampleRencanaKinerja(1L), sampleRencanaKinerja(2L));
        when(rencanaKinerjaService.findAll()).thenReturn(data);

        Iterable<RencanaKinerja> result = rencanaKinerjaController.findAll();

        assertThat(result).containsExactlyElementsOf(data);
        verify(rencanaKinerjaService).findAll();
    }

    @Test
    void put_updatesEntityUsingService() {
        Long id = 3L;
        RencanaKinerja existing = new RencanaKinerja(
                id,
                "Rencana Lama",
                "Indikator Lama",
                "Target Lama",
                "APBD",
                "Keterangan Lama",
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z")
        );
        RencanaKinerjaRequest request = new RencanaKinerjaRequest(
                null,
                "Rencana Baru",
                "Indikator Baru",
                "Target Baru",
                "DAK",
                "Keterangan Baru"
        );
        RencanaKinerja updated = new RencanaKinerja(
                id,
                request.rencanaKinerja(),
                request.indikator(),
                request.target(),
                request.sumberDana(),
                request.keterangan(),
                existing.createdDate(),
                Instant.parse("2024-01-03T00:00:00Z")
        );

        when(rencanaKinerjaService.detailRencanaKinerjaById(id)).thenReturn(existing);
        when(rencanaKinerjaService.ubahRencanaKinerja(eq(id), any(RencanaKinerja.class))).thenReturn(updated);

        RencanaKinerja result = rencanaKinerjaController.put(id, request);

        ArgumentCaptor<RencanaKinerja> captor = ArgumentCaptor.forClass(RencanaKinerja.class);
        verify(rencanaKinerjaService).detailRencanaKinerjaById(id);
        verify(rencanaKinerjaService).ubahRencanaKinerja(eq(id), captor.capture());

        RencanaKinerja passed = captor.getValue();
        assertEquals(existing.id(), passed.id());
        assertEquals(request.rencanaKinerja(), passed.rencanaKinerja());
        assertEquals(request.indikator(), passed.indikator());
        assertEquals(request.target(), passed.target());
        assertEquals(request.sumberDana(), passed.sumberDana());
        assertEquals(request.keterangan(), passed.keterangan());
        assertEquals(existing.createdDate(), passed.createdDate());
        assertNull(passed.lastModifiedDate());
        assertThat(result).isEqualTo(updated);
    }

    @Test
    void post_createsEntityAndReturnsCreatedResponse() {
        RencanaKinerjaRequest request = new RencanaKinerjaRequest(
                null,
                "Rencana Baru",
                "Indikator Baru",
                "Target Baru",
                "DAU",
                "Keterangan Baru"
        );
        RencanaKinerja saved = new RencanaKinerja(
                7L,
                request.rencanaKinerja(),
                request.indikator(),
                request.target(),
                request.sumberDana(),
                request.keterangan(),
                Instant.parse("2024-02-01T00:00:00Z"),
                Instant.parse("2024-02-01T00:00:00Z")
        );
        when(rencanaKinerjaService.tambahRencanaKinerja(any(RencanaKinerja.class))).thenReturn(saved);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/rencanakinerja");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<RencanaKinerja> response = rencanaKinerjaController.post(request);

        ArgumentCaptor<RencanaKinerja> captor = ArgumentCaptor.forClass(RencanaKinerja.class);
        verify(rencanaKinerjaService).tambahRencanaKinerja(captor.capture());

        RencanaKinerja passed = captor.getValue();
        assertNull(passed.id());
        assertEquals(request.rencanaKinerja(), passed.rencanaKinerja());
        assertEquals(request.indikator(), passed.indikator());
        assertEquals(request.target(), passed.target());
        assertEquals(request.sumberDana(), passed.sumberDana());
        assertEquals(request.keterangan(), passed.keterangan());
        assertNull(passed.createdDate());
        assertNull(passed.lastModifiedDate());

        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isEqualTo(saved);

        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location != null ? location.getPath() : null).isEqualTo("/rencanakinerja/" + saved.id());
    }

    @Test
    void delete_removesEntityUsingService() {
        Long id = 4L;

        rencanaKinerjaController.delete(id);

        verify(rencanaKinerjaService).hapusRencanaKinerja(id);
    }

    private RencanaKinerja sampleRencanaKinerja(Long id) {
        Instant fixed = Instant.parse("2024-01-01T00:00:00Z");
        return new RencanaKinerja(
                id,
                "Rencana " + id,
                "Indikator " + id,
                "Target " + id,
                "APBD",
                "Keterangan",
                fixed,
                fixed
        );
    }
}
