package cc.kertaskerja.bontang.programprioritas.web;

import cc.kertaskerja.bontang.programprioritas.domain.ProgramPrioritas;
import cc.kertaskerja.bontang.programprioritas.domain.ProgramPrioritasService;
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
public class ProgramPrioritasControllerTest {

    @Mock
    private ProgramPrioritasService programPrioritasService;

    private ProgramPrioritasController programPrioritasController;

    @BeforeEach
    void setUp() {
        programPrioritasController = new ProgramPrioritasController(programPrioritasService);
    }

    @Test
    void findAll_returnsAllProgramPrioritasFromService() {
        Iterable<ProgramPrioritas> programPrioritasList = List.of(
                new ProgramPrioritas(1L, "Program Prioritas 1", Instant.now(), Instant.now()),
                new ProgramPrioritas(2L, "Program Prioritas 2", Instant.now(), Instant.now())
        );

        when(programPrioritasService.findAll()).thenReturn(programPrioritasList);

        Iterable<ProgramPrioritas> result = programPrioritasController.findAll();

        assertEquals(programPrioritasList, result);
        verify(programPrioritasService).findAll();
    }

    @Test
    void getById_returnsProgramPrioritasFromService() {
        Long id = 1L;
        ProgramPrioritas programPrioritas = new ProgramPrioritas(id, "Program Prioritas 1", Instant.now(), Instant.now());

        when(programPrioritasService.detailProgramPrioritasById(id)).thenReturn(programPrioritas);

        ProgramPrioritas result = programPrioritasController.getById(id);

        assertEquals(programPrioritas, result);
        verify(programPrioritasService).detailProgramPrioritasById(id);
    }

    @Test
    void put_updatesProgramPrioritasUsingService() {
        Long id = 1L;
        ProgramPrioritas existingProgramPrioritas = new ProgramPrioritas(id, "Program Prioritas 1", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));
        ProgramPrioritasRequest request = new ProgramPrioritasRequest(null, "Program Prioritas Updated");
        ProgramPrioritas updatedProgramPrioritas = new ProgramPrioritas(id, request.programPrioritas(), existingProgramPrioritas.createdDate(), Instant.parse("2024-01-03T00:00:00Z"));

        when(programPrioritasService.detailProgramPrioritasById(id)).thenReturn(existingProgramPrioritas);
        when(programPrioritasService.ubahProgramPrioritas(eq(id), any(ProgramPrioritas.class))).thenReturn(updatedProgramPrioritas);

        ProgramPrioritas result = programPrioritasController.put(id, request);

        ArgumentCaptor<ProgramPrioritas> programPrioritasCaptor = ArgumentCaptor.forClass(ProgramPrioritas.class);
        verify(programPrioritasService).detailProgramPrioritasById(id);
        verify(programPrioritasService).ubahProgramPrioritas(eq(id), programPrioritasCaptor.capture());

        ProgramPrioritas programPrioritasPassed = programPrioritasCaptor.getValue();
        assertEquals(existingProgramPrioritas.id(), programPrioritasPassed.id());
        assertEquals(request.programPrioritas(), programPrioritasPassed.programPrioritas());
        assertEquals(existingProgramPrioritas.createdDate(), programPrioritasPassed.createdDate());
        assertNull(programPrioritasPassed.lastModifiedDate());

        assertEquals(updatedProgramPrioritas, result);
    }

    @Test
    void post_createsProgramPrioritasAndReturnsCreatedResponse() {
        ProgramPrioritasRequest request = new ProgramPrioritasRequest(null, "Program Prioritas Baru");
        ProgramPrioritas savedProgramPrioritas = new ProgramPrioritas(1L, request.programPrioritas(), Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(programPrioritasService.tambahProgramPrioritas(any(ProgramPrioritas.class))).thenReturn(savedProgramPrioritas);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/programprioritas");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<ProgramPrioritas> response = programPrioritasController.post(request);

        ArgumentCaptor<ProgramPrioritas> programPrioritasCaptor = ArgumentCaptor.forClass(ProgramPrioritas.class);
        verify(programPrioritasService).tambahProgramPrioritas(programPrioritasCaptor.capture());

        ProgramPrioritas programPrioritasPassed = programPrioritasCaptor.getValue();
        assertNull(programPrioritasPassed.id());
        assertEquals(request.programPrioritas(), programPrioritasPassed.programPrioritas());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedProgramPrioritas, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/programprioritas/" + savedProgramPrioritas.id(), location != null ? location.getPath() : null);
    }

    @Test
    void delete_deletesProgramPrioritasUsingService() {
        Long id = 1L;

        programPrioritasController.delete(id);

        verify(programPrioritasService).hapusProgramPrioritas(id);
    }
}
