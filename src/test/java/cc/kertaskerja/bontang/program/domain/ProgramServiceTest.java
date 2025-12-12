package cc.kertaskerja.bontang.program.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanRepository;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
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
    @Mock
    private BidangUrusanRepository bidangUrusanRepository;

    private ProgramService programService;

    @BeforeEach
    void setUp() {
        programService = new ProgramService(programRepository, bidangUrusanRepository);
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
    void detailProgramByKodeProgramIn_returnsPrograms_whenAllFound() {
        List<String> kodePrograms = Arrays.asList("PR-001", "PR-002");
        Program program1 = new Program(1L, "PR-001", "Program 1", Instant.now(), Instant.now());
        Program program2 = new Program(2L, "PR-002", "Program 2", Instant.now(), Instant.now());

        when(programRepository.findAllByKodeProgramIn(kodePrograms)).thenReturn(Arrays.asList(program1, program2));

        List<Program> result = programService.detailProgramByKodeProgramIn(kodePrograms);

        assertEquals(2, result.size());
        assertEquals(program1, result.get(0));
        assertEquals(program2, result.get(1));
        verify(programRepository).findAllByKodeProgramIn(kodePrograms);
    }

    @Test
    void detailProgramByKodeProgramIn_throwsException_whenProgramMissing() {
        List<String> kodePrograms = Arrays.asList("PR-001", "PR-002");
        Program program1 = new Program(1L, "PR-001", "Program 1", Instant.now(), Instant.now());

        when(programRepository.findAllByKodeProgramIn(kodePrograms)).thenReturn(List.of(program1));

        ProgramNotFoundException exception = assertThrows(ProgramNotFoundException.class, () -> programService.detailProgramByKodeProgramIn(kodePrograms));
        assertEquals("Program dengan kode PR-002 tidak ditemukan.", exception.getMessage());
        verify(programRepository).findAllByKodeProgramIn(kodePrograms);
    }

    @Test
    void tambahProgram_savesProgram() {
        Program program = Program.of("PR-001", "Program 1");
        Program savedProgram = new Program(1L, "PR-001", "Program 1", Instant.now(), Instant.now());

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

    @Test
    void getKodeBidangUrusan_returnsKode_whenIdExists() {
        Long bidangUrusanId = 77L;
        BidangUrusan bidangUrusan = new BidangUrusan(bidangUrusanId, "OPD-01", "BU-01", "Bidang Infrastruktur", Instant.now(), Instant.now());
        when(bidangUrusanRepository.findById(bidangUrusanId)).thenReturn(Optional.of(bidangUrusan));

        String result = programService.getKodeBidangUrusan(bidangUrusanId);

        assertEquals(bidangUrusan.kodeBidangUrusan(), result);
        verify(bidangUrusanRepository).findById(bidangUrusanId);
    }

    @Test
    void getKodeBidangUrusan_throwsException_whenIdNotFound() {
        Long bidangUrusanId = 999L;
        when(bidangUrusanRepository.findById(bidangUrusanId)).thenReturn(Optional.empty());

        assertThrows(BidangUrusanNotFoundException.class, () -> programService.getKodeBidangUrusan(bidangUrusanId));
        verify(bidangUrusanRepository).findById(bidangUrusanId);
    }
}
