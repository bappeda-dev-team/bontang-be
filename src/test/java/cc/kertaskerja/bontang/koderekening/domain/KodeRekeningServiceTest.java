package cc.kertaskerja.bontang.koderekening.domain;

import cc.kertaskerja.bontang.koderekening.domain.exception.KodeRekeningNotFoundException;
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
public class KodeRekeningServiceTest {

    @Mock
    private KodeRekeningRepository kodeRekeningRepository;

    private KodeRekeningService kodeRekeningService;

    @BeforeEach
    void setUp() {
        kodeRekeningService = new KodeRekeningService(kodeRekeningRepository);
    }

    @Test
    void findAll_returnsAllKodeRekening() {
        KodeRekening kodeRekening1 = new KodeRekening(1L, "KR-001", "Kode Rekening 1", Instant.now(), Instant.now());
        KodeRekening kodeRekening2 = new KodeRekening(2L, "KR-002", "Kode Rekening 2", Instant.now(), Instant.now());
        List<KodeRekening> kodeRekeningList = Arrays.asList(kodeRekening1, kodeRekening2);

        when(kodeRekeningRepository.findAll()).thenReturn(kodeRekeningList);

        Iterable<KodeRekening> result = kodeRekeningService.findAll();

        assertEquals(kodeRekeningList, result);
        verify(kodeRekeningRepository).findAll();
    }

    @Test
    void detailKodeRekeningByKodeRekening_returnsKodeRekening_whenFound() {
        String kodeRekening = "KR-001";
        KodeRekening expectedKodeRekening = new KodeRekening(1L, kodeRekening, "Kode Rekening 1", Instant.now(), Instant.now());
        when(kodeRekeningRepository.findByKodeRekening(kodeRekening)).thenReturn(Optional.of(expectedKodeRekening));

        KodeRekening result = kodeRekeningService.detailKodeRekeningByKodeRekening(kodeRekening);

        assertEquals(expectedKodeRekening, result);
        verify(kodeRekeningRepository).findByKodeRekening(kodeRekening);
    }

    @Test
    void detailKodeRekeningByKodeRekening_throwsException_whenNotFound() {
        String kodeRekening = "KR-404";
        when(kodeRekeningRepository.findByKodeRekening(kodeRekening)).thenReturn(Optional.empty());

        assertThrows(KodeRekeningNotFoundException.class, () -> kodeRekeningService.detailKodeRekeningByKodeRekening(kodeRekening));
        verify(kodeRekeningRepository).findByKodeRekening(kodeRekening);
    }

    @Test
    void tambahKodeRekening_savesKodeRekening() {
        String kodeRekening = "KR-001";
        KodeRekening kodeRekeningBaru = KodeRekening.of(kodeRekening, "Kode Rekening 1");
        KodeRekening savedKodeRekening = new KodeRekening(1L, kodeRekening, "Kode Rekening 1", Instant.now(), Instant.now());

        when(kodeRekeningRepository.save(kodeRekeningBaru)).thenReturn(savedKodeRekening);

        KodeRekening result = kodeRekeningService.tambahKodeRekening(kodeRekeningBaru);

        assertEquals(savedKodeRekening, result);
        verify(kodeRekeningRepository).save(kodeRekeningBaru);
    }

    @Test
    void ubahKodeRekening_savesKodeRekening_whenKodeRekeningExists() {
        String kodeRekening = "KR-001";
        KodeRekening kodeRekeningUpdate = KodeRekening.of(kodeRekening, "Kode Rekening 1");
        KodeRekening updatedKodeRekening = new KodeRekening(1L, kodeRekening, "Kode Rekening 1 Updated", Instant.now(), Instant.now());

        when(kodeRekeningRepository.existsByKodeRekening(kodeRekening)).thenReturn(true);
        when(kodeRekeningRepository.save(kodeRekeningUpdate)).thenReturn(updatedKodeRekening);

        KodeRekening result = kodeRekeningService.ubahKodeRekening(kodeRekening, kodeRekeningUpdate);

        assertEquals(updatedKodeRekening, result);
        verify(kodeRekeningRepository).existsByKodeRekening(kodeRekening);
        verify(kodeRekeningRepository).save(kodeRekeningUpdate);
    }

    @Test
    void ubahKodeRekening_throwsException_whenKodeRekeningNotExists() {
        String kodeRekening = "KR-404";
        KodeRekening kodeRekeningUpdate = KodeRekening.of(kodeRekening, "Kode Rekening 404");

        when(kodeRekeningRepository.existsByKodeRekening(kodeRekening)).thenReturn(false);

        assertThrows(KodeRekeningNotFoundException.class, () -> kodeRekeningService.ubahKodeRekening(kodeRekening, kodeRekeningUpdate));
        verify(kodeRekeningRepository).existsByKodeRekening(kodeRekening);
        verify(kodeRekeningRepository, never()).save(any());
    }

    @Test
    void hapusKodeRekening_deletesKodeRekening_whenKodeRekeningExists() {
        String kodeRekening = "KR-001";

        when(kodeRekeningRepository.existsByKodeRekening(kodeRekening)).thenReturn(true);

        kodeRekeningService.hapusKodeRekening(kodeRekening);

        verify(kodeRekeningRepository).existsByKodeRekening(kodeRekening);
        verify(kodeRekeningRepository).deleteByKodeRekening(kodeRekening);
    }

    @Test
    void hapusKodeRekening_throwsException_whenKodeRekeningNotExists() {
        String kodeRekening = "KR-404";

        when(kodeRekeningRepository.existsByKodeRekening(kodeRekening)).thenReturn(false);

        assertThrows(KodeRekeningNotFoundException.class, () -> kodeRekeningService.hapusKodeRekening(kodeRekening));
        verify(kodeRekeningRepository).existsByKodeRekening(kodeRekening);
        verify(kodeRekeningRepository, never()).deleteByKodeRekening(any());
    }
}
