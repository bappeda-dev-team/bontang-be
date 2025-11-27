package cc.kertaskerja.bontang.program.web;

import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.program.domain.ProgramService;
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
public class ProgramControllerTest {

    @Mock
    private ProgramService programService;

    private ProgramController programController;

    @BeforeEach
    void setUp() {
        programController = new ProgramController(programService);
    }

    @Test
    void findAll_returnsAllProgramsFromService() {
        Iterable<Program> programs = List.of(
                new Program(1L, "PR-001", "Program 1", 10L, Instant.now(), Instant.now()),
                new Program(2L, "PR-002", "Program 2", 11L, Instant.now(), Instant.now())
        );

        when(programService.findAll()).thenReturn(programs);

        Iterable<Program> result = programController.findAll();

        assertEquals(programs, result);
        verify(programService).findAll();
    }

    @Test
    void getByKodeProgram_returnsProgramFromService() {
        String kodeProgram = "PR-001";
        Program program = new Program(1L, kodeProgram, "Program 1", 10L, Instant.now(), Instant.now());

        when(programService.detailProgramByKodeProgram(kodeProgram)).thenReturn(program);

        Program result = programController.getByKodeProgram(kodeProgram);

        assertEquals(program, result);
        verify(programService).detailProgramByKodeProgram(kodeProgram);
    }

    @Test
    void put_updatesProgramUsingService() {
        String kodeProgram = "PR-001";
        String kodeBidangUrusan = "BU-01";
        Program existingProgram = new Program(1L, kodeProgram, "Program 1", 20L, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-02T00:00:00Z"));
        ProgramRequest request = new ProgramRequest(null, "PR-002", "Program Updated", kodeBidangUrusan);
        Program updatedProgram = new Program(1L, request.kodeProgram(), request.namaProgram(), 30L, existingProgram.createdDate(), Instant.parse("2024-01-03T00:00:00Z"));

        when(programService.detailProgramByKodeProgram(kodeProgram)).thenReturn(existingProgram);
        when(programService.ubahProgram(eq(kodeProgram), any(Program.class), eq(kodeBidangUrusan))).thenReturn(updatedProgram);

        Program result = programController.put(kodeProgram, request);

        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);
        verify(programService).detailProgramByKodeProgram(kodeProgram);
        verify(programService).ubahProgram(eq(kodeProgram), programCaptor.capture(), eq(kodeBidangUrusan));

        Program programPassed = programCaptor.getValue();
        assertEquals(existingProgram.id(), programPassed.id());
        assertEquals(request.kodeProgram(), programPassed.kodeProgram());
        assertEquals(request.namaProgram(), programPassed.namaProgram());
        assertEquals(existingProgram.bidangUrusanId(), programPassed.bidangUrusanId());
        assertEquals(existingProgram.createdDate(), programPassed.createdDate());
        assertNull(programPassed.lastModifiedDate());

        assertEquals(updatedProgram, result);
    }

    @Test
    void post_createsProgramAndReturnsCreatedResponse() {
        String kodeBidangUrusan = "BU-01";
        ProgramRequest request = new ProgramRequest(null, "PR-001", "Program 1", kodeBidangUrusan);
        Program savedProgram = new Program(1L, request.kodeProgram(), request.namaProgram(), 10L, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-01T00:00:00Z"));

        when(programService.tambahProgram(any(Program.class), eq(kodeBidangUrusan))).thenReturn(savedProgram);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/program");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(servletRequest));

        ResponseEntity<Program> response = programController.post(request);

        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);
        verify(programService).tambahProgram(programCaptor.capture(), eq(kodeBidangUrusan));

        Program programPassed = programCaptor.getValue();
        assertNull(programPassed.id());
        assertEquals(request.kodeProgram(), programPassed.kodeProgram());
        assertEquals(request.namaProgram(), programPassed.namaProgram());
        assertNull(programPassed.bidangUrusanId());

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedProgram, response.getBody());

        URI location = response.getHeaders().getLocation();
        assertEquals("/program/" + savedProgram.id(), location != null ? location.getPath() : null);
    }

    @Test
    void delete_deletesProgramUsingService() {
        String kodeProgram = "PR-001";

        programController.delete(kodeProgram);

        verify(programService).hapusProgram(kodeProgram);
    }
}
