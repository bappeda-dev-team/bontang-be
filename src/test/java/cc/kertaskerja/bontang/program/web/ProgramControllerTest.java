package cc.kertaskerja.bontang.program.web;

import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.program.domain.ProgramService;
import cc.kertaskerja.bontang.program.web.request.ProgramBatchRequest;
import cc.kertaskerja.bontang.program.web.request.ProgramRequest;
import cc.kertaskerja.bontang.program.web.response.ProgramResponse;

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
        Instant program1Created = Instant.parse("2024-01-01T00:00:00Z");
        Instant program1Updated = Instant.parse("2024-01-02T00:00:00Z");
        Instant program2Created = Instant.parse("2024-02-01T00:00:00Z");
        Instant program2Updated = Instant.parse("2024-02-02T00:00:00Z");
        Program program1 = new Program(1L, "PR-001", "Program 1", 10L, program1Created, program1Updated);
        Program program2 = new Program(2L, "PR-002", "Program 2", 11L, program2Created, program2Updated);
        Iterable<Program> programs = List.of(program1, program2);

        when(programService.findAll()).thenReturn(programs);
        when(programService.getKodeBidangUrusan(10L)).thenReturn("BU-10");
        when(programService.getKodeBidangUrusan(11L)).thenReturn("BU-11");

        List<ProgramResponse> result = programController.findAll();

        assertEquals(2, result.size());
        ProgramResponse firstResponse = result.get(0);
        ProgramResponse secondResponse = result.get(1);
        assertEquals(program1.id(), firstResponse.id());
        assertEquals(program1.kodeProgram(), firstResponse.kodeProgram());
        assertEquals(program1.namaProgram(), firstResponse.namaProgram());
        assertEquals("BU-10", firstResponse.kodeBidangUrusan());
        assertEquals(program1Created, firstResponse.createdDate());
        assertEquals(program1Updated, firstResponse.lastModifiedDate());
        assertEquals(program2.id(), secondResponse.id());
        assertEquals(program2.kodeProgram(), secondResponse.kodeProgram());
        assertEquals(program2.namaProgram(), secondResponse.namaProgram());
        assertEquals("BU-11", secondResponse.kodeBidangUrusan());
        assertEquals(program2Created, secondResponse.createdDate());
        assertEquals(program2Updated, secondResponse.lastModifiedDate());
        verify(programService).findAll();
        verify(programService).getKodeBidangUrusan(10L);
        verify(programService).getKodeBidangUrusan(11L);
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
    void findBatch_returnsProgramsFromService() {
        ProgramBatchRequest request = new ProgramBatchRequest(List.of("PR-001", "PR-002"));
        String kodeOpd = "OPD-01";
        List<Program> programs = List.of(
                new Program(1L, "PR-001", "Program 1", 10L, Instant.now(), Instant.now()),
                new Program(2L, "PR-002", "Program 2", 11L, Instant.now(), Instant.now())
        );

        when(programService.detailProgramByKodeProgramInAndKodeOpd(request.kodeProgram(), kodeOpd)).thenReturn(programs);

        List<Program> result = programController.findBatch(kodeOpd, request);

        assertEquals(programs, result);
        verify(programService).detailProgramByKodeProgramInAndKodeOpd(request.kodeProgram(), kodeOpd);
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
