package cc.kertaskerja.bontang.program.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanRepository;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import cc.kertaskerja.bontang.kegiatan.domain.KegiatanRepository;
import cc.kertaskerja.bontang.program.domain.exception.ProgramDeleteForbiddenException;
import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    @Mock
    private KegiatanRepository kegiatanRepository;

    private ProgramService programService;

    @BeforeEach
    void setUp() {
        programService = new ProgramService(programRepository, bidangUrusanRepository, kegiatanRepository);
    }

    @Test
    void findAll_returnsAllPrograms() {
        Program program1 = new Program(1L, "PR-001", "Program 1", 10L, Instant.now(), Instant.now());
        Program program2 = new Program(2L, "PR-002", "Program 2", 20L, Instant.now(), Instant.now());
        List<Program> programList = Arrays.asList(program1, program2);

        when(programRepository.findAll()).thenReturn(programList);

        Iterable<Program> result = programService.findAll();

        assertEquals(programList, result);
        verify(programRepository).findAll();
    }

    @Test
    void detailProgramByKodeProgram_returnsProgram_whenFound() {
        String kodeProgram = "PR-001";
        Program program = new Program(1L, kodeProgram, "Program 1", 10L, Instant.now(), Instant.now());
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
        String kodeBidangUrusan = "BU-01";
        BidangUrusan bidangUrusan = new BidangUrusan(99L, "OPD-1", kodeBidangUrusan, "Bidang 1", Instant.now(), Instant.now());
        Program program = Program.of(kodeProgram, "Program 1", null);
        Program savedProgram = new Program(1L, kodeProgram, "Program 1", bidangUrusan.id(), Instant.now(), Instant.now());

        when(bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)).thenReturn(Optional.of(bidangUrusan));
        when(programRepository.save(any(Program.class))).thenReturn(savedProgram);

        Program result = programService.tambahProgram(program, kodeBidangUrusan);

        assertEquals(savedProgram, result);
        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);
        verify(bidangUrusanRepository).findByKodeBidangUrusan(kodeBidangUrusan);
        verify(programRepository).save(programCaptor.capture());
        assertEquals(bidangUrusan.id(), programCaptor.getValue().bidangUrusanId());
    }

    @Test
    void tambahProgram_throwsException_whenBidangUrusanNotFound() {
        String kodeBidangUrusan = "BU-XX";
        Program program = Program.of("PR-001", "Program 1", null);

        when(bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)).thenReturn(Optional.empty());

        assertThrows(BidangUrusanNotFoundException.class, () -> programService.tambahProgram(program, kodeBidangUrusan));
        verify(bidangUrusanRepository).findByKodeBidangUrusan(kodeBidangUrusan);
        verify(programRepository, never()).save(any());
    }

    @Test
    void ubahProgram_savesProgram_whenKodeProgramExists() {
        String kodeProgram = "PR-001";
        String kodeBidangUrusan = "BU-01";
        BidangUrusan bidangUrusan = new BidangUrusan(88L, "OPD-2", kodeBidangUrusan, "Bidang 1", Instant.now(), Instant.now());
        Program program = Program.of(kodeProgram, "Program 1", null);
        Program updatedProgram = new Program(1L, kodeProgram, "Program 1 Updated", bidangUrusan.id(), Instant.now(), Instant.now());

        when(programRepository.existsByKodeProgram(kodeProgram)).thenReturn(true);
        when(bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)).thenReturn(Optional.of(bidangUrusan));
        when(programRepository.save(any(Program.class))).thenReturn(updatedProgram);

        Program result = programService.ubahProgram(kodeProgram, program, kodeBidangUrusan);

        assertEquals(updatedProgram, result);
        verify(programRepository).existsByKodeProgram(kodeProgram);
        verify(bidangUrusanRepository).findByKodeBidangUrusan(kodeBidangUrusan);
        verify(programRepository).save(any(Program.class));
    }

    @Test
    void ubahProgram_throwsException_whenKodeProgramNotExists() {
        String kodeProgram = "PR-404";
        Program program = Program.of(kodeProgram, "Program 1", null);

        when(programRepository.existsByKodeProgram(kodeProgram)).thenReturn(false);

        assertThrows(ProgramNotFoundException.class, () -> programService.ubahProgram(kodeProgram, program, "BU-01"));
        verify(programRepository).existsByKodeProgram(kodeProgram);
        verify(programRepository, never()).save(any());
    }

    @Test
    void hapusProgram_deletesProgram_whenKodeProgramExists() {
        String kodeProgram = "PR-001";
        Program program = new Program(1L, kodeProgram, "Program 1", 10L, Instant.now(), Instant.now());

        when(programRepository.findByKodeProgram(kodeProgram)).thenReturn(Optional.of(program));
        when(kegiatanRepository.existsByProgramId(program.id())).thenReturn(false);

        programService.hapusProgram(kodeProgram);

        verify(programRepository).findByKodeProgram(kodeProgram);
        verify(kegiatanRepository).existsByProgramId(program.id());
        verify(programRepository).deleteByKodeProgram(kodeProgram);
    }

    @Test
    void hapusProgram_throwsException_whenKodeProgramNotExists() {
        String kodeProgram = "PR-404";

        when(programRepository.findByKodeProgram(kodeProgram)).thenReturn(Optional.empty());

        assertThrows(ProgramNotFoundException.class, () -> programService.hapusProgram(kodeProgram));
        verify(programRepository).findByKodeProgram(kodeProgram);
        verify(kegiatanRepository, never()).existsByProgramId(any());
        verify(programRepository, never()).deleteByKodeProgram(any());
    }

    @Test
    void hapusProgram_throwsException_whenProgramHasKegiatan() {
        String kodeProgram = "PR-001";
        Program program = new Program(1L, kodeProgram, "Program 1", 10L, Instant.now(), Instant.now());

        when(programRepository.findByKodeProgram(kodeProgram)).thenReturn(Optional.of(program));
        when(kegiatanRepository.existsByProgramId(program.id())).thenReturn(true);

        assertThrows(ProgramDeleteForbiddenException.class, () -> programService.hapusProgram(kodeProgram));
        verify(programRepository).findByKodeProgram(kodeProgram);
        verify(kegiatanRepository).existsByProgramId(program.id());
        verify(programRepository, never()).deleteByKodeProgram(any());
    }
}
