package cc.kertaskerja.bontang.subkegiatan.domain;

import cc.kertaskerja.bontang.kegiatan.domain.Kegiatan;
import cc.kertaskerja.bontang.kegiatan.domain.KegiatanRepository;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanNotFoundException;
import cc.kertaskerja.bontang.subkegiatan.domain.exception.SubKegiatanNotFoundException;
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
public class SubKegiatanServiceTest {

    @Mock
    private SubKegiatanRepository subKegiatanRepository;
    @Mock
    private KegiatanRepository kegiatanRepository;

    private SubKegiatanService subKegiatanService;

    @BeforeEach
    void setUp() {
        subKegiatanService = new SubKegiatanService(subKegiatanRepository, kegiatanRepository);
    }

    @Test
    void findAll_returnsAllSubKegiatan() {
        SubKegiatan subKegiatan1 = new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", 10L, Instant.now(), Instant.now());
        SubKegiatan subKegiatan2 = new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", 20L, Instant.now(), Instant.now());
        List<SubKegiatan> subKegiatanList = Arrays.asList(subKegiatan1, subKegiatan2);

        when(subKegiatanRepository.findAll()).thenReturn(subKegiatanList);

        Iterable<SubKegiatan> result = subKegiatanService.findAll();

        assertEquals(subKegiatanList, result);
        verify(subKegiatanRepository).findAll();
    }

    @Test
    void findAllByKodeOpd_returnsSubKegiatansForOpd() {
        String kodeOpd = "OPD-1";
        SubKegiatan subKegiatan1 = new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", 10L, Instant.now(), Instant.now());
        SubKegiatan subKegiatan2 = new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", 10L, Instant.now(), Instant.now());
        List<SubKegiatan> subKegiatanList = Arrays.asList(subKegiatan1, subKegiatan2);

        when(subKegiatanRepository.findAllByKodeOpd(kodeOpd)).thenReturn(subKegiatanList);

        List<SubKegiatan> result = subKegiatanService.findAllByKodeOpd(kodeOpd);

        assertEquals(subKegiatanList, result);
        verify(subKegiatanRepository).findAllByKodeOpd(kodeOpd);
    }

    @Test
    void detailSubKegiatanByKodeSubKegiatan_returnsSubKegiatan_whenFound() {
        String kodeSubKegiatan = "SK-001";
        SubKegiatan subKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1", 10L, Instant.now(), Instant.now());
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
    void tambahSubKegiatan_savesSubKegiatan() {
        String kodeSubKegiatan = "SK-001";
        String kodeKegiatan = "KG-01";
        Kegiatan kegiatan = new Kegiatan(11L, kodeKegiatan, "Kegiatan 1", Instant.now(), Instant.now());
        SubKegiatan subKegiatan = SubKegiatan.of(kodeSubKegiatan, "Sub Kegiatan 1", null);
        SubKegiatan savedSubKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1", kegiatan.id(), Instant.now(), Instant.now());

        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.of(kegiatan));
        when(subKegiatanRepository.save(any(SubKegiatan.class))).thenReturn(savedSubKegiatan);

        SubKegiatan result = subKegiatanService.tambahSubKegiatan(subKegiatan, kodeKegiatan);

        assertEquals(savedSubKegiatan, result);
        verify(kegiatanRepository).findByKodeKegiatan(kodeKegiatan);
        ArgumentCaptor<SubKegiatan> subKegiatanCaptor = ArgumentCaptor.forClass(SubKegiatan.class);
        verify(subKegiatanRepository).save(subKegiatanCaptor.capture());
        assertEquals(kegiatan.id(), subKegiatanCaptor.getValue().kegiatanId());
    }

    @Test
    void ubahSubKegiatan_savesSubKegiatan_whenKodeSubKegiatanExists() {
        String kodeSubKegiatan = "SK-001";
        String kodeKegiatan = "KG-01";
        Kegiatan kegiatan = new Kegiatan(12L, kodeKegiatan, "Kegiatan 1", Instant.now(), Instant.now());
        Instant createdDate = Instant.now();
        Instant lastModifiedDate = Instant.now();
        SubKegiatan subKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1", null, createdDate, lastModifiedDate);
        SubKegiatan updatedSubKegiatan = new SubKegiatan(1L, kodeSubKegiatan, "Sub Kegiatan 1 Updated", kegiatan.id(), Instant.now(), Instant.now());

        when(subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(true);
        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.of(kegiatan));
        when(subKegiatanRepository.save(any(SubKegiatan.class))).thenReturn(updatedSubKegiatan);

        SubKegiatan result = subKegiatanService.ubahSubKegiatan(kodeSubKegiatan, subKegiatan, kodeKegiatan);

        assertEquals(updatedSubKegiatan, result);
        verify(subKegiatanRepository).existsByKodeSubKegiatan(kodeSubKegiatan);
        verify(kegiatanRepository).findByKodeKegiatan(kodeKegiatan);
        ArgumentCaptor<SubKegiatan> subKegiatanCaptor = ArgumentCaptor.forClass(SubKegiatan.class);
        verify(subKegiatanRepository).save(subKegiatanCaptor.capture());
        SubKegiatan savedArg = subKegiatanCaptor.getValue();
        assertEquals(subKegiatan.id(), savedArg.id());
        assertEquals(subKegiatan.kodeSubKegiatan(), savedArg.kodeSubKegiatan());
        assertEquals(subKegiatan.namaSubKegiatan(), savedArg.namaSubKegiatan());
        assertEquals(kegiatan.id(), savedArg.kegiatanId());
        assertEquals(createdDate, savedArg.createdDate());
        assertEquals(lastModifiedDate, savedArg.lastModifiedDate());
    }

    @Test
    void ubahSubKegiatan_throwsException_whenKodeSubKegiatanNotExists() {
        String kodeSubKegiatan = "SK-404";
        SubKegiatan subKegiatan = SubKegiatan.of(kodeSubKegiatan, "Sub Kegiatan 1", null);

        when(subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(false);

        assertThrows(SubKegiatanNotFoundException.class, () -> subKegiatanService.ubahSubKegiatan(kodeSubKegiatan, subKegiatan, "KG-01"));
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

    @Test
    void hapusSubKegiatanByOpd_deletesWhenFound() {
        String kodeOpd = "OPD-001";
        String kodeSubKegiatan = "SK-001";
        SubKegiatan subKegiatan = new SubKegiatan(99L, kodeSubKegiatan, "Sub Kegiatan", 10L, Instant.now(), Instant.now());

        when(subKegiatanRepository.findByKodeSubKegiatanAndKodeOpd(kodeSubKegiatan, kodeOpd))
                .thenReturn(Optional.of(subKegiatan));

        subKegiatanService.hapusSubKegiatan(kodeOpd, kodeSubKegiatan);

        verify(subKegiatanRepository).findByKodeSubKegiatanAndKodeOpd(kodeSubKegiatan, kodeOpd);
        verify(subKegiatanRepository).deleteById(subKegiatan.id());
    }

    @Test
    void hapusSubKegiatanByOpd_throwsException_whenNotFound() {
        String kodeOpd = "OPD-002";
        String kodeSubKegiatan = "SK-404";

        when(subKegiatanRepository.findByKodeSubKegiatanAndKodeOpd(kodeSubKegiatan, kodeOpd))
                .thenReturn(Optional.empty());

        assertThrows(SubKegiatanNotFoundException.class, () -> subKegiatanService.hapusSubKegiatan(kodeOpd, kodeSubKegiatan));
        verify(subKegiatanRepository).findByKodeSubKegiatanAndKodeOpd(kodeSubKegiatan, kodeOpd);
        verify(subKegiatanRepository, never()).deleteById(any());
    }

    @Test
    void detailSubKegiatanIn_returnsSubKegiatans_whenAllFound() {
        List<String> kodeSubKegiatans = Arrays.asList("SK-001", "SK-002");
        List<SubKegiatan> subKegiatanList = Arrays.asList(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", 10L, Instant.now(), Instant.now()),
                new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", 10L, Instant.now(), Instant.now())
        );

        when(subKegiatanRepository.findAllByKodeSubKegiatanIn(kodeSubKegiatans)).thenReturn(subKegiatanList);

        List<SubKegiatan> result = subKegiatanService.detailSubKegiatanIn(kodeSubKegiatans);

        assertEquals(subKegiatanList, result);
        verify(subKegiatanRepository).findAllByKodeSubKegiatanIn(kodeSubKegiatans);
    }

    @Test
    void detailSubKegiatanIn_throwsException_whenMissingKode() {
        List<String> kodeSubKegiatans = Arrays.asList("SK-001", "SK-002");
        List<SubKegiatan> foundSubKegiatan = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", 10L, Instant.now(), Instant.now())
        );

        when(subKegiatanRepository.findAllByKodeSubKegiatanIn(kodeSubKegiatans)).thenReturn(foundSubKegiatan);

        assertThrows(SubKegiatanNotFoundException.class, () -> subKegiatanService.detailSubKegiatanIn(kodeSubKegiatans));
        verify(subKegiatanRepository).findAllByKodeSubKegiatanIn(kodeSubKegiatans);
    }

    @Test
    void detailSubKegiatanByAndKodeSubKegiatanIn_returnsSubKegiatans_whenAllFound() {
        List<String> kodeSubKegiatans = Arrays.asList("SK-001", "SK-002");
        List<SubKegiatan> subKegiatanList = Arrays.asList(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", 10L, Instant.now(), Instant.now()),
                new SubKegiatan(2L, "SK-002", "Sub Kegiatan 2", 10L, Instant.now(), Instant.now())
        );

        when(subKegiatanRepository.findAllByKodeSubKegiatanIn(kodeSubKegiatans)).thenReturn(subKegiatanList);

        List<SubKegiatan> result = subKegiatanService.detailSubKegiatanByAndKodeSubKegiatanIn(kodeSubKegiatans);

        assertEquals(subKegiatanList, result);
        verify(subKegiatanRepository).findAllByKodeSubKegiatanIn(kodeSubKegiatans);
    }

    @Test
    void detailSubKegiatanByAndKodeSubKegiatanIn_throwsException_whenMissingKode() {
        List<String> kodeSubKegiatans = Arrays.asList("SK-001", "SK-002");
        List<SubKegiatan> foundSubKegiatan = List.of(
                new SubKegiatan(1L, "SK-001", "Sub Kegiatan 1", 10L, Instant.now(), Instant.now())
        );

        when(subKegiatanRepository.findAllByKodeSubKegiatanIn(kodeSubKegiatans)).thenReturn(foundSubKegiatan);

        assertThrows(SubKegiatanNotFoundException.class, () -> subKegiatanService.detailSubKegiatanByAndKodeSubKegiatanIn(kodeSubKegiatans));
        verify(subKegiatanRepository).findAllByKodeSubKegiatanIn(kodeSubKegiatans);
    }

    @Test
    void getKodeKegiatan_returnsKode_whenFound() {
        Long kegiatanId = 55L;
        Kegiatan kegiatan = new Kegiatan(kegiatanId, "KG-001", "Kegiatan 1", Instant.now(), Instant.now());

        when(kegiatanRepository.findById(kegiatanId)).thenReturn(Optional.of(kegiatan));

        String kodeKegiatan = subKegiatanService.getKodeKegiatan(kegiatanId);

        assertEquals(kegiatan.kodeKegiatan(), kodeKegiatan);
        verify(kegiatanRepository).findById(kegiatanId);
    }

    @Test
    void getKodeKegiatan_throwsException_whenKegiatanNotFound() {
        Long kegiatanId = 77L;

        when(kegiatanRepository.findById(kegiatanId)).thenReturn(Optional.empty());

        assertThrows(KegiatanNotFoundException.class, () -> subKegiatanService.getKodeKegiatan(kegiatanId));
        verify(kegiatanRepository).findById(kegiatanId);
    }

    @Test
    void tambahSubKegiatan_throwsException_whenKegiatanNotFound() {
        String kodeSubKegiatan = "SK-001";
        String kodeKegiatan = "KG-404";
        SubKegiatan subKegiatan = SubKegiatan.of(kodeSubKegiatan, "Sub Kegiatan 1", null);

        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.empty());

        assertThrows(KegiatanNotFoundException.class, () -> subKegiatanService.tambahSubKegiatan(subKegiatan, kodeKegiatan));
        verify(kegiatanRepository).findByKodeKegiatan(kodeKegiatan);
        verify(subKegiatanRepository, never()).save(any());
    }

    @Test
    void ubahSubKegiatan_throwsException_whenKegiatanNotFound() {
        String kodeSubKegiatan = "SK-001";
        String kodeKegiatan = "KG-404";
        SubKegiatan subKegiatan = SubKegiatan.of(kodeSubKegiatan, "Sub Kegiatan 1", null);

        when(subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)).thenReturn(true);
        when(kegiatanRepository.findByKodeKegiatan(kodeKegiatan)).thenReturn(Optional.empty());

        assertThrows(KegiatanNotFoundException.class, () -> subKegiatanService.ubahSubKegiatan(kodeSubKegiatan, subKegiatan, kodeKegiatan));
        verify(subKegiatanRepository).existsByKodeSubKegiatan(kodeSubKegiatan);
        verify(kegiatanRepository).findByKodeKegiatan(kodeKegiatan);
        verify(subKegiatanRepository, never()).save(any());
    }
}
