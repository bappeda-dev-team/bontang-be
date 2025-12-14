package cc.kertaskerja.bontang.kegiatan.domain;

import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KegiatanServiceTest {

    @Mock
    private KegiatanRepository kegiatanRepository;

    private KegiatanService kegiatanService;

    @BeforeEach
    void setUp() {
        kegiatanService = new KegiatanService(kegiatanRepository);
    }

    @Test
    void findAll_returnsAllKegiatan() {
        Kegiatan kegiatan1 = new Kegiatan(1L, "KG-001", "Kegiatan 1", Instant.now(), Instant.now());
        Kegiatan kegiatan2 = new Kegiatan(2L, "KG-002", "Kegiatan 2", Instant.now(), Instant.now());
        List<Kegiatan> kegiatanList = List.of(kegiatan1, kegiatan2);

        when(kegiatanRepository.findAll()).thenReturn(kegiatanList);

        Iterable<Kegiatan> result = kegiatanService.findAll();

        assertEquals(kegiatanList, result);
        verify(kegiatanRepository).findAll();
    }

    @Test
    void detailKegiatanByKodeKegiatan_returnsKegiatan_whenFound() {
        String kodeKegiatan = "KG-001";
        Kegiatan kegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1", Instant.now(), Instant.now());
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
    void detailKegiatanByKodeKegiatanIn_returnsKegiatans_whenAllFound() {
        List<String> kodeKegiatans = List.of("KG-001", "KG-002");
        Kegiatan kegiatan1 = new Kegiatan(1L, "KG-001", "Kegiatan 1", Instant.now(), Instant.now());
        Kegiatan kegiatan2 = new Kegiatan(2L, "KG-002", "Kegiatan 2", Instant.now(), Instant.now());
        List<Kegiatan> kegiatanList = List.of(kegiatan1, kegiatan2);

        when(kegiatanRepository.findAllByKodeKegiatanIn(kodeKegiatans)).thenReturn(kegiatanList);

        List<Kegiatan> result = kegiatanService.detailKegiatanByKodeKegiatanIn(kodeKegiatans);

        assertEquals(kegiatanList, result);
        verify(kegiatanRepository).findAllByKodeKegiatanIn(kodeKegiatans);
    }

    @Test
    void detailKegiatanByKodeKegiatanIn_throwsException_whenMissingOne() {
        List<String> kodeKegiatans = List.of("KG-001", "KG-002");
        Kegiatan kegiatan1 = new Kegiatan(1L, "KG-001", "Kegiatan 1", Instant.now(), Instant.now());

        when(kegiatanRepository.findAllByKodeKegiatanIn(kodeKegiatans)).thenReturn(List.of(kegiatan1));

        assertThrows(KegiatanNotFoundException.class, () -> kegiatanService.detailKegiatanByKodeKegiatanIn(kodeKegiatans));
        verify(kegiatanRepository).findAllByKodeKegiatanIn(kodeKegiatans);
    }

    @Test
    void tambahKegiatan_savesKegiatan() {
        Kegiatan kegiatan = Kegiatan.of("KG-001", "Kegiatan 1");
        Kegiatan savedKegiatan = new Kegiatan(1L, "KG-001", "Kegiatan 1", Instant.now(), Instant.now());

        when(kegiatanRepository.save(kegiatan)).thenReturn(savedKegiatan);

        Kegiatan result = kegiatanService.tambahKegiatan(kegiatan);

        assertEquals(savedKegiatan, result);
        verify(kegiatanRepository).save(kegiatan);
    }

    @Test
    void ubahKegiatan_savesKegiatan_whenKodeKegiatanExists() {
        String kodeKegiatan = "KG-001";
        Kegiatan kegiatan = Kegiatan.of(kodeKegiatan, "Kegiatan 1");
        Kegiatan updatedKegiatan = new Kegiatan(1L, kodeKegiatan, "Kegiatan 1 Updated", Instant.now(), Instant.now());

        when(kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)).thenReturn(true);
        when(kegiatanRepository.save(kegiatan)).thenReturn(updatedKegiatan);

        Kegiatan result = kegiatanService.ubahKegiatan(kodeKegiatan, kegiatan);

        assertEquals(updatedKegiatan, result);
        verify(kegiatanRepository).existsByKodeKegiatan(kodeKegiatan);
        verify(kegiatanRepository).save(kegiatan);
    }

    @Test
    void ubahKegiatan_throwsException_whenKodeKegiatanNotExists() {
        String kodeKegiatan = "KG-404";
        Kegiatan kegiatan = Kegiatan.of(kodeKegiatan, "Kegiatan 1");

        when(kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)).thenReturn(false);

        assertThrows(KegiatanNotFoundException.class, () -> kegiatanService.ubahKegiatan(kodeKegiatan, kegiatan));
        verify(kegiatanRepository).existsByKodeKegiatan(kodeKegiatan);
        verify(kegiatanRepository, never()).save(any());
    }

    @Test
    void hapusKegiatan_deletesKegiatan_whenKodeKegiatanExists() {
        String kodeKegiatan = "KG-001";
        when(kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)).thenReturn(true);

        kegiatanService.hapusKegiatan(kodeKegiatan);

        verify(kegiatanRepository).existsByKodeKegiatan(kodeKegiatan);
        verify(kegiatanRepository).deleteByKodeKegiatan(kodeKegiatan);
    }

    @Test
    void hapusKegiatan_throwsException_whenKodeKegiatanNotExists() {
        String kodeKegiatan = "KG-404";

        when(kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)).thenReturn(false);

        assertThrows(KegiatanNotFoundException.class, () -> kegiatanService.hapusKegiatan(kodeKegiatan));
        verify(kegiatanRepository).existsByKodeKegiatan(kodeKegiatan);
        verify(kegiatanRepository, never()).deleteByKodeKegiatan(any());
    }
}
