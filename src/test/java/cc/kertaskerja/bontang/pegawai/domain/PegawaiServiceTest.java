package cc.kertaskerja.bontang.pegawai.domain;

import cc.kertaskerja.bontang.opd.domain.Opd;
import cc.kertaskerja.bontang.opd.domain.OpdRepository;
import cc.kertaskerja.bontang.opd.domain.exception.OpdNotFoundException;
import cc.kertaskerja.bontang.pegawai.domain.exception.PegawaiNotFoundException;
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
public class PegawaiServiceTest {

    @Mock
    private PegawaiRepository pegawaiRepository;
    @Mock
    private OpdRepository opdRepository;

    private PegawaiService pegawaiService;

    @BeforeEach
    void setUp() {
        pegawaiService = new PegawaiService(pegawaiRepository, opdRepository);
    }

    @Test
    void findAll_returnsAllPegawai() {
        Pegawai pegawai1 = new Pegawai(1L, 10L, "John Doe", "12345", "john@example.com", "Staff", "Tim A", Instant.now(), Instant.now());
        Pegawai pegawai2 = new Pegawai(2L, 20L, "Jane Doe", "67890", "jane@example.com", "Manager", "Tim B", Instant.now(), Instant.now());
        List<Pegawai> pegawaiList = Arrays.asList(pegawai1, pegawai2);

        when(pegawaiRepository.findAll()).thenReturn(pegawaiList);

        Iterable<Pegawai> result = pegawaiService.findAll();

        assertEquals(pegawaiList, result);
        verify(pegawaiRepository).findAll();
    }

    @Test
    void detailPegawaiByNip_returnsPegawai_whenFound() {
        String nip = "12345";
        Pegawai pegawai = new Pegawai(1L, 10L, "John Doe", nip, "john@example.com", "Staff", "Tim A", Instant.now(), Instant.now());
        when(pegawaiRepository.findByNip(nip)).thenReturn(Optional.of(pegawai));

        Pegawai result = pegawaiService.detailPegawaiByNip(nip);

        assertEquals(pegawai, result);
        verify(pegawaiRepository).findByNip(nip);
    }

    @Test
    void detailPegawaiByNip_throwsException_whenNotFound() {
        String nip = "99999";
        when(pegawaiRepository.findByNip(nip)).thenReturn(Optional.empty());

        assertThrows(PegawaiNotFoundException.class, () -> pegawaiService.detailPegawaiByNip(nip));
        verify(pegawaiRepository).findByNip(nip);
    }

    @Test
    void tambahPegawai_savesPegawaiWithOpd() {
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(101L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());
        Pegawai pegawai = Pegawai.of("John Doe", "12345", "john@example.com", "Staff", "Tim A", null);
        Pegawai savedPegawai = new Pegawai(1L, opd.id(), "John Doe", "12345", "john@example.com", "Staff", "Tim A", Instant.now(), Instant.now());

        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.of(opd));
        when(pegawaiRepository.save(any(Pegawai.class))).thenReturn(savedPegawai);

        Pegawai result = pegawaiService.tambahPegawai(pegawai, kodeOpd);

        assertEquals(savedPegawai, result);
        ArgumentCaptor<Pegawai> pegawaiCaptor = ArgumentCaptor.forClass(Pegawai.class);
        verify(opdRepository).findByKodeOpd(kodeOpd);
        verify(pegawaiRepository).save(pegawaiCaptor.capture());
        assertEquals(opd.id(), pegawaiCaptor.getValue().opdId());
    }

    @Test
    void tambahPegawai_throwsException_whenOpdNotFound() {
        String kodeOpd = "OPD-404";
        Pegawai pegawai = Pegawai.of("John Doe", "12345", "john@example.com", "Staff", "Tim A", null);

        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.empty());

        assertThrows(OpdNotFoundException.class, () -> pegawaiService.tambahPegawai(pegawai, kodeOpd));
        verify(opdRepository).findByKodeOpd(kodeOpd);
        verify(pegawaiRepository, never()).save(any());
    }

    @Test
    void ubahPegawai_savesPegawai_whenNipExists() {
        String nip = "12345";
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(202L, kodeOpd, "BPKAD", Instant.now(), Instant.now());
        Pegawai pegawai = Pegawai.of("John Doe", nip, "john@example.com", "Staff", "Tim A", null);
        Pegawai updatedPegawai = new Pegawai(2L, opd.id(), "John Doe", nip, "john@example.com", "Staff", "Tim A", Instant.now(), Instant.now());

        when(pegawaiRepository.existsByNip(nip)).thenReturn(true);
        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.of(opd));
        when(pegawaiRepository.save(any(Pegawai.class))).thenReturn(updatedPegawai);

        Pegawai result = pegawaiService.ubahPegawai(nip, pegawai, kodeOpd);

        assertEquals(updatedPegawai, result);
        verify(pegawaiRepository).existsByNip(nip);
        verify(opdRepository).findByKodeOpd(kodeOpd);
        verify(pegawaiRepository).save(any(Pegawai.class));
    }

    @Test
    void ubahPegawai_throwsException_whenNipNotExists() {
        String nip = "99999";
        Pegawai pegawai = Pegawai.of("John Doe", nip, "john@example.com", "Staff", "Tim A", null);

        when(pegawaiRepository.existsByNip(nip)).thenReturn(false);

        assertThrows(PegawaiNotFoundException.class, () -> pegawaiService.ubahPegawai(nip, pegawai, "OPD-001"));
        verify(pegawaiRepository).existsByNip(nip);
        verify(opdRepository, never()).findByKodeOpd(any());
        verify(pegawaiRepository, never()).save(any());
    }

    @Test
    void ubahPegawai_throwsException_whenOpdNotFound() {
        String nip = "12345";
        String kodeOpd = "OPD-404";
        Pegawai pegawai = Pegawai.of("John Doe", nip, "john@example.com", "Staff", "Tim A", null);

        when(pegawaiRepository.existsByNip(nip)).thenReturn(true);
        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.empty());

        assertThrows(OpdNotFoundException.class, () -> pegawaiService.ubahPegawai(nip, pegawai, kodeOpd));
        verify(pegawaiRepository).existsByNip(nip);
        verify(opdRepository).findByKodeOpd(kodeOpd);
        verify(pegawaiRepository, never()).save(any());
    }

    @Test
    void hapusPegawai_deletesPegawai_whenNipExists() {
        String nip = "12345";

        when(pegawaiRepository.existsByNip(nip)).thenReturn(true);

        pegawaiService.hapusPegawai(nip);

        verify(pegawaiRepository).existsByNip(nip);
        verify(pegawaiRepository).deleteByNip(nip);
    }

    @Test
    void hapusPegawai_throwsException_whenNipNotExists() {
        String nip = "99999";

        when(pegawaiRepository.existsByNip(nip)).thenReturn(false);

        assertThrows(PegawaiNotFoundException.class, () -> pegawaiService.hapusPegawai(nip));
        verify(pegawaiRepository).existsByNip(nip);
        verify(pegawaiRepository, never()).deleteByNip(any());
    }
}
