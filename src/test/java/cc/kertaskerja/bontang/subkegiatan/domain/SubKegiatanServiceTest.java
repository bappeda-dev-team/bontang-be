package cc.kertaskerja.bontang.subkegiatan.domain;

import cc.kertaskerja.bontang.subkegiatan.domain.exception.SubKegiatanNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubKegiatanServiceTest {

    @Mock
    private SubKegiatanRepository subKegiatanRepository;

    private SubKegiatanService subKegiatanService;

    @BeforeEach
    void setUp() {
        subKegiatanService = new SubKegiatanService(subKegiatanRepository);
    }

    @Test
    void findAll_returnsAllSubKegiatan() {
        Instant createdDate = Instant.parse("2024-01-01T00:00:00Z");
        List<SubKegiatan> subKegiatanList = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", createdDate, createdDate),
                new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", createdDate, createdDate)
        );

        when(subKegiatanRepository.findAll()).thenReturn(subKegiatanList);

        Iterable<SubKegiatan> result = subKegiatanService.findAll();

        assertIterableEquals(subKegiatanList, result);
        verify(subKegiatanRepository).findAll();
    }

    @Test
    void detailSubKegiatanByKodeSubKegiatan_returnsSubKegiatan_whenFound() {
        String kodeSubKegiatan = "SK-001";
        SubKegiatan subKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1", Instant.now(), Instant.now());
        when(subKegiatanRepository.findByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(Optional.of(subKegiatan));

        SubKegiatan result = subKegiatanService.detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan);

        assertEquals(subKegiatan, result);
        verify(subKegiatanRepository).findByKodeSubKegiatan(kodeSubKegiatan);
    }

    @Test
    void detailSubKegiatanByKodeSubKegiatan_throwsException_whenNotFound() {
        String kodeSubKegiatan = "SK-404";
        when(subKegiatanRepository.findByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(Optional.empty());

        assertThrows(SubKegiatanNotFoundException.class, () -> subKegiatanService.detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan));
        verify(subKegiatanRepository).findByKodeSubKegiatan(kodeSubKegiatan);
    }

    @Test
    void detailSubKegiatanIn_returnsSubKegiatans_whenAllFound() {
        List<String> kodeSubKegiatans = List.of("SK-001", "SK-002");
        List<SubKegiatan> subKegiatanList = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", Instant.now(), Instant.now()),
                new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", Instant.now(), Instant.now())
        );

        when(subKegiatanRepository.findAllByKodeSubKegiatanIn(kodeSubKegiatans)).thenReturn(subKegiatanList);

        List<SubKegiatan> result = subKegiatanService.detailSubKegiatanIn(kodeSubKegiatans);

        assertEquals(subKegiatanList, result);
        verify(subKegiatanRepository).findAllByKodeSubKegiatanIn(kodeSubKegiatans);
    }

    @Test
    void detailSubKegiatanIn_throwsException_whenMissingKode() {
        List<String> kodeSubKegiatans = List.of("SK-001", "SK-002");
        List<SubKegiatan> foundSubKegiatan = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", Instant.now(), Instant.now())
        );

        when(subKegiatanRepository.findAllByKodeSubKegiatanIn(kodeSubKegiatans)).thenReturn(foundSubKegiatan);

        assertThrows(SubKegiatanNotFoundException.class, () -> subKegiatanService.detailSubKegiatanIn(kodeSubKegiatans));
        verify(subKegiatanRepository).findAllByKodeSubKegiatanIn(kodeSubKegiatans);
    }

    @Test
    void tambahSubKegiatan_savesSubKegiatan() {
        SubKegiatan subKegiatan = SubKegiatan.of("SK-001", "Sub Kegiatan 1");
        SubKegiatan savedSubKegiatan = new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", Instant.now(), Instant.now());

        when(subKegiatanRepository.save(subKegiatan)).thenReturn(savedSubKegiatan);

        SubKegiatan result = subKegiatanService.tambahSubKegiatan(subKegiatan);

        assertEquals(savedSubKegiatan, result);
        verify(subKegiatanRepository).save(subKegiatan);
    }

    @Test
    void ubahSubKegiatan_savesSubKegiatan_whenKodeSubKegiatanExists() {
        String kodeSubKegiatan = "SK-001";
        SubKegiatan updatedSubKegiatan = new SubKegiatan(5L, kodeSubKegiatan, "Sub Kegiatan Updated", Instant.now(), Instant.now());

        when(subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(true);
        when(subKegiatanRepository.save(updatedSubKegiatan)).thenReturn(updatedSubKegiatan);

        SubKegiatan result = subKegiatanService.ubahSubKegiatan(kodeSubKegiatan, updatedSubKegiatan);

        assertEquals(updatedSubKegiatan, result);
        verify(subKegiatanRepository).existsByKodeSubKegiatan(kodeSubKegiatan);
        verify(subKegiatanRepository).save(updatedSubKegiatan);
    }

    @Test
    void ubahSubKegiatan_throwsException_whenKodeSubKegiatanNotExists() {
        String kodeSubKegiatan = "SK-404";
        SubKegiatan subKegiatan = SubKegiatan.of(kodeSubKegiatan, "Sub Kegiatan 1");

        when(subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(false);

        assertThrows(SubKegiatanNotFoundException.class, () -> subKegiatanService.ubahSubKegiatan(kodeSubKegiatan, subKegiatan));
        verify(subKegiatanRepository).existsByKodeSubKegiatan(kodeSubKegiatan);
        verify(subKegiatanRepository, never()).save(any());
    }

    @Test
    void hapusSubKegiatan_deletesSubKegiatan_whenKodeSubKegiatanExists() {
        String kodeSubKegiatan = "SK-001";

        when(subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(true);

        subKegiatanService.hapusSubKegiatan(kodeSubKegiatan);

        verify(subKegiatanRepository).existsByKodeSubKegiatan(kodeSubKegiatan);
        verify(subKegiatanRepository).deleteByKodeSubKegiatan(kodeSubKegiatan);
    }

    @Test
    void hapusSubKegiatan_throwsException_whenKodeSubKegiatanNotExists() {
        String kodeSubKegiatan = "SK-404";

        when(subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(false);

        assertThrows(SubKegiatanNotFoundException.class, () -> subKegiatanService.hapusSubKegiatan(kodeSubKegiatan));
        verify(subKegiatanRepository).existsByKodeSubKegiatan(kodeSubKegiatan);
        verify(subKegiatanRepository, never()).deleteByKodeSubKegiatan(any());
    }
}
