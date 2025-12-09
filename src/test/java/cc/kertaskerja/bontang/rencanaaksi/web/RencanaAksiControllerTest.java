package cc.kertaskerja.bontang.rencanaaksi.web;

import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksiService;
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
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RencanaAksiControllerTest {

    @Mock
    private RencanaAksiService rencanaAksiService;

    private RencanaAksiController rencanaAksiController;

    @BeforeEach
    void setUp() {
        rencanaAksiController = new RencanaAksiController(rencanaAksiService);
    }

    @Test
    void getById_returnsEntityFromService() {
        Long id = 1L;
        RencanaAksi expected = sampleRencanaAksi(id);
        when(rencanaAksiService.detailRencanaAksiById(id)).thenReturn(expected);

        RencanaAksi result = rencanaAksiController.getById(id);

        assertThat(result).isEqualTo(expected);
        verify(rencanaAksiService).detailRencanaAksiById(id);
    }

    @Test
    void findAll_returnsAllEntities() {
        List<RencanaAksi> data = List.of(sampleRencanaAksi(1L), sampleRencanaAksi(2L));
        when(rencanaAksiService.findAll()).thenReturn(data);

        Iterable<RencanaAksi> result = rencanaAksiController.findAll();

        assertThat(result).containsExactlyElementsOf(data);
        verify(rencanaAksiService).findAll();
    }

    @Test
    void put_updatesEntityUsingService() {
        Long id = 3L;
        RencanaAksi existing = new RencanaAksi(
                id,
                "Rencana Lama",
                1,
                Instant.parse("2024-01-01T00:00:00Z"),
                Instant.parse("2024-01-02T00:00:00Z")
        );
        RencanaAksiRequest request = new RencanaAksiRequest(
                null,
                "Rencana Baru",
                5
        );
        RencanaAksi updated = new RencanaAksi(
                id,
                request.rencanaAksi(),
                request.urutan(),
                existing.createdDate(),
                Instant.parse("2024-01-03T00:00:00Z")
        );

        when(rencanaAksiService.detailRencanaAksiById(id)).thenReturn(existing);
        when(rencanaAksiService.ubahRencanaAksi(eq(id), any(RencanaAksi.class))).thenReturn(updated);

        RencanaAksi result = rencanaAksiController.put(id, request);

        ArgumentCaptor<RencanaAksi> captor = ArgumentCaptor.forClass(RencanaAksi.class);
        verify(rencanaAksiService).detailRencanaAksiById(id);
        verify(rencanaAksiService).ubahRencanaAksi(eq(id), captor.capture());

        RencanaAksi passed = captor.getValue();
        assertEquals(existing.id(), passed.id());
        assertEquals(request.rencanaAksi(), passed.rencanaAksi());
        assertEquals(request.urutan(), passed.urutan());
        assertEquals(existing.createdDate(), passed.createdDate());
        assertNull(passed.lastModifiedDate());
        assertThat(result).isEqualTo(updated);
    }

    @Test
    void post_createsEntityAndReturnsCreatedResponse() {
        RencanaAksiRequest request = new RencanaAksiRequest(
                null,
                "Rencana Baru",
                7
        );
        RencanaAksi saved = new RencanaAksi(
                9L,
                request.rencanaAksi(),
                request.urutan(),
                Instant.parse("2024-02-01T00:00:00Z"),
                Instant.parse("2024-02-01T00:00:00Z")
        );
        when(rencanaAksiService.tambahRencanaAksi(any(RencanaAksi.class))).thenReturn(saved);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/rencanaaksi");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<RencanaAksi> response = rencanaAksiController.post(request);

        ArgumentCaptor<RencanaAksi> captor = ArgumentCaptor.forClass(RencanaAksi.class);
        verify(rencanaAksiService).tambahRencanaAksi(captor.capture());

        RencanaAksi passed = captor.getValue();
        assertNull(passed.id());
        assertEquals(request.rencanaAksi(), passed.rencanaAksi());
        assertEquals(request.urutan(), passed.urutan());
        assertNull(passed.createdDate());
        assertNull(passed.lastModifiedDate());

        assertEquals(201, response.getStatusCodeValue());
        assertThat(response.getBody()).isEqualTo(saved);

        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location != null ? location.getPath() : null).isEqualTo("/rencanaaksi/" + saved.id());
    }

    @Test
    void delete_removesEntityUsingService() {
        Long id = 4L;

        rencanaAksiController.delete(id);

        verify(rencanaAksiService).hapusRencanaAksi(id);
    }

    private RencanaAksi sampleRencanaAksi(Long id) {
        Instant fixed = Instant.parse("2024-01-01T00:00:00Z");
        return new RencanaAksi(
                id,
                "Rencana " + id,
                id.intValue(),
                fixed,
                fixed
        );
    }
}
