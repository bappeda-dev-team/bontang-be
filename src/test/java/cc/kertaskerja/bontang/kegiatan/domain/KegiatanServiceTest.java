package cc.kertaskerja.bontang.kegiatan.domain;

import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanAlreadyExistException;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanDeleteForbiddenException;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanNotFoundException;
import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.program.domain.ProgramRepository;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatanRepository;
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
public class KegiatanServiceTest {

    @Mock
    private KegiatanRepository kegiatanRepository;
    @Mock
    private ProgramRepository programRepository;
    @Mock
    private SubKegiatanRepository subKegiatanRepository;

    private KegiatanService kegiatanService;

    @BeforeEach
    void setUp() {
        kegiatanService = new KegiatanService(kegiatanRepository, programRepository, subKegiatanRepository);
    }

    @Test
    void findAll_returnsAllKegiatan() {
        Kegiatan kegiatan1 = new Kegiatan(1L, "KG-001", "Kegiatan 1", 10L, Instant.now(), Instant.now());
        Kegiatan kegiatan2 = new Kegiatan(2L, "KG-002", "Kegiatan 2", 20L, Instant.now(), Instant.now());
        List<Kegiatan> kegiatanList = Arrays.asList(kegiatan1, kegiatan2);

        when(kegiatanRepository.findAll()).thenReturn(kegiatanList);

        Iterable<Kegiatan> result = kegiatanService.findAll();

        assertEquals(kegiatanList, result);
        verify(kegiatanRepository).findAll();
    }

    @Test
    void detailKegiatanByKodeKegiatan_returnsKegiatan_whenFound() {
        String kodeKegiatan = "KG-001";
        Kegiatan kegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1", 10L, Instant.now(), Instant.now());
        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.of(kegiatan));

        Kegiatan result = kegiatanService.detailKegiatanByKodeKegiatan(kodeKegiatan);

        assertEquals(kegiatan, result);
        verify(kegiatanRepository).findByKodeKegiatan(kodeKegiatan);
    }

    @Test
    void detailKegiatanByKodeKegiatan_throwsException_whenNotFound() {
        String kodeKegiatan = "KG-404";
        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.empty());

        assertThrows(KegiatanNotFoundException.class, () -> kegiatanService.detailKegiatanByKodeKegiatan(kodeKegiatan));
        verify(kegiatanRepository).findByKodeKegiatan(kodeKegiatan);
    }

    @Test
    void tambahKegiatan_savesKegiatan_whenKodeKegiatanNotExists() {
        String kodeKegiatan = "KG-001";
        String kodeProgram = "PR-001";
        Program program = new Program(10L, kodeProgram, "Program 1", 100L, Instant.now(), Instant.now());
        Kegiatan kegiatan = Kegiatan.of(kodeKegiatan, "Kegiatan 1", null);
        Kegiatan savedKegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1", program.id(), Instant.now(), Instant.now());

        when(kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)).thenReturn(false);
        when(programRepository.findByKodeProgram(kodeProgram)).thenReturn(Optional.of(program));
        when(kegiatanRepository.save(any(Kegiatan.class))).thenReturn(savedKegiatan);

        Kegiatan result = kegiatanService.tambahKegiatan(kegiatan, kodeProgram);

        assertEquals(savedKegiatan, result);
        verify(kegiatanRepository).existsByKodeKegiatan(kodeKegiatan);
        verify(programRepository).findByKodeProgram(kodeProgram);
        ArgumentCaptor<Kegiatan> kegiatanCaptor = ArgumentCaptor.forClass(Kegiatan.class);
        verify(kegiatanRepository).save(kegiatanCaptor.capture());
        assertEquals(program.id(), kegiatanCaptor.getValue().programId());
    }

    @Test
    void tambahKegiatan_throwsException_whenKodeKegiatanAlreadyExists() {
        String kodeKegiatan = "KG-001";
        Kegiatan kegiatan = Kegiatan.of(kodeKegiatan, "Kegiatan 1", null);

        when(kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)).thenReturn(true);

        assertThrows(KegiatanAlreadyExistException.class, () -> kegiatanService.tambahKegiatan(kegiatan, "PR-01"));
        verify(kegiatanRepository).existsByKodeKegiatan(kodeKegiatan);
        verify(kegiatanRepository, never()).save(any());
    }

    @Test
    void ubahKegiatan_savesKegiatan_whenKodeKegiatanExists() {
        String kodeKegiatan = "KG-001";
        String kodeProgram = "PR-001";
        Program program = new Program(20L, kodeProgram, "Program 1", 101L, Instant.now(), Instant.now());
        Kegiatan kegiatan = Kegiatan.of(kodeKegiatan, "Kegiatan 1", null);
        Kegiatan updatedKegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1 Updated", program.id(), Instant.now(), Instant.now());

        when(kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)).thenReturn(true);
        when(programRepository.findByKodeProgram(kodeProgram)).thenReturn(Optional.of(program));
        when(kegiatanRepository.save(any(Kegiatan.class))).thenReturn(updatedKegiatan);

        Kegiatan result = kegiatanService.ubahKegiatan(kodeKegiatan, kegiatan, kodeProgram);

        assertEquals(updatedKegiatan, result);
        verify(kegiatanRepository).existsByKodeKegiatan(kodeKegiatan);
        verify(programRepository).findByKodeProgram(kodeProgram);
        verify(kegiatanRepository).save(any(Kegiatan.class));
    }

    @Test
    void ubahKegiatan_throwsException_whenKodeKegiatanNotExists() {
        String kodeKegiatan = "KG-404";
        Kegiatan kegiatan = Kegiatan.of(kodeKegiatan, "Kegiatan 1", null);

        when(kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)).thenReturn(false);

        assertThrows(KegiatanNotFoundException.class, () -> kegiatanService.ubahKegiatan(kodeKegiatan, kegiatan, "PR-01"));
        verify(kegiatanRepository).existsByKodeKegiatan(kodeKegiatan);
        verify(kegiatanRepository, never()).save(any());
    }

    @Test
    void hapusKegiatan_deletesKegiatan_whenTidakAdaSubKegiatan() {
        String kodeKegiatan = "KG-001";
        Kegiatan kegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1", 10L, Instant.now(), Instant.now());

        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.of(kegiatan));
        when(subKegiatanRepository.existsByKegiatanId(kegiatan.id())).thenReturn(false);

        kegiatanService.hapusKegiatan(kodeKegiatan);

        verify(kegiatanRepository).findByKodeKegiatan(kodeKegiatan);
        verify(subKegiatanRepository).existsByKegiatanId(kegiatan.id());
        verify(kegiatanRepository).deleteByKodeKegiatan(kodeKegiatan);
    }

    @Test
    void hapusKegiatan_throwsException_whenKodeKegiatanNotExists() {
        String kodeKegiatan = "KG-404";

        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.empty());

        assertThrows(KegiatanNotFoundException.class, () -> kegiatanService.hapusKegiatan(kodeKegiatan));
        verify(kegiatanRepository).findByKodeKegiatan(kodeKegiatan);
        verify(kegiatanRepository, never()).deleteByKodeKegiatan(any());
    }

    @Test
    void hapusKegiatan_throwsForbidden_whenAdaSubKegiatan() {
        String kodeKegiatan = "KG-001";
        Kegiatan kegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1", 10L, Instant.now(), Instant.now());

        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.of(kegiatan));
        when(subKegiatanRepository.existsByKegiatanId(kegiatan.id())).thenReturn(true);

        assertThrows(KegiatanDeleteForbiddenException.class, () -> kegiatanService.hapusKegiatan(kodeKegiatan));

        verify(subKegiatanRepository).existsByKegiatanId(kegiatan.id());
        verify(kegiatanRepository, never()).deleteByKodeKegiatan(any());
    }
}
