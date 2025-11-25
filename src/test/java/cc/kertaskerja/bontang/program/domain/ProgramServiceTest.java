package cc.kertaskerja.bontang.program.domain;

import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    private ProgramService programService;

    @BeforeEach
    void setUp() {
        programService = new ProgramService(programRepository);
    }

    @Test
    void findAll_returnsAllPrograms() {
        Program program1 = new Program(1L, "PR-001", "Program 1", Instant.now(), Instant.now());
        Program program2 = new Program(2L, "PR-002", "Program 2", Instant.now(), Instant.now());
        List<Program> programList = Arrays.asList(program1, program2);

        when(programRepository.findAll()).thenReturn(programList);

        Iterable<Program> result = programService.findAll();

        assertEquals(programList, result);
        verify(programRepository).findAll();
    }

    @Test
    void detailProgramByKodeProgram_returnsProgram_whenFound() {
        String kodeProgram = "PR-001";
        Program program = new Program(1L, kodeProgram, "Program 1", Instant.now(), Instant.now());
        when(programRepository.findByKodeProgram(kodeProgram)).thenReturn(Optional.of(program));

        Program result = programService.detailProgramByKodeProgram(kodeProgram);

        assertEquals(program, result);
        verify(programRepository).findByKodeProgram(kodeProgram);
    }

    @Test
    void detailProgramByKodeProgram_throwsException_whenNotFound() {
        String kodeProgram = "PR-404";
        when(programRepository.findByKodeProgram(kodeProgram)).thenReturn(Optional.empty());

        assertThrows(ProgramNotFoundException.class, () -> programService.detailProgramByKodeProgram(kodeProgram));
        verify(programRepository).findByKodeProgram(kodeProgram);
    }

    @Test
    void tambahProgram_savesProgram() {
        String kodeProgram = "PR-001";
        Program program = Program.of(kodeProgram, "Program 1");
        Program savedProgram = new Program(1L, kodeProgram, "Program 1", Instant.now(), Instant.now());

        when(programRepository.save(program)).thenReturn(savedProgram);

        Program result = programService.tambahProgram(program);

        assertEquals(savedProgram, result);
        verify(programRepository).save(program);
    }

    @Test
    void ubahProgram_savesProgram_whenKodeProgramExists() {
        String kodeProgram = "PR-001";
        Program program = Program.of(kodeProgram, "Program 1");
        Program updatedProgram = new Program(1L, kodeProgram, "Program 1 Updated", Instant.now(), Instant.now());

        when(programRepository.existsByKodeProgram(kodeProgram)).thenReturn(true);
        when(programRepository.save(program)).thenReturn(updatedProgram);

        Program result = programService.ubahProgram(kodeProgram, program);

        assertEquals(updatedProgram, result);
        verify(programRepository).existsByKodeProgram(kodeProgram);
        verify(programRepository).save(program);
    }

    @Test
    void ubahProgram_throwsException_whenKodeProgramNotExists() {
        String kodeProgram = "PR-404";
        Program program = Program.of(kodeProgram, "Program 1");

        when(programRepository.existsByKodeProgram(kodeProgram)).thenReturn(false);

        assertThrows(ProgramNotFoundException.class, () -> programService.ubahProgram(kodeProgram, program));
        verify(programRepository).existsByKodeProgram(kodeProgram);
        verify(programRepository, never()).save(any());
    }

    @Test
    void hapusProgram_deletesProgram_whenKodeProgramExists() {
        String kodeProgram = "PR-001";

        when(programRepository.existsByKodeProgram(kodeProgram)).thenReturn(true);

        programService.hapusProgram(kodeProgram);

        verify(programRepository).existsByKodeProgram(kodeProgram);
        verify(programRepository).deleteByKodeProgram(kodeProgram);
    }

    @Test
    void hapusProgram_throwsException_whenKodeProgramNotExists() {
        String kodeProgram = "PR-404";

        when(programRepository.existsByKodeProgram(kodeProgram)).thenReturn(false);

        assertThrows(ProgramNotFoundException.class, () -> programService.hapusProgram(kodeProgram));
        verify(programRepository).existsByKodeProgram(kodeProgram);
        verify(programRepository, never()).deleteByKodeProgram(any());
    }
}
