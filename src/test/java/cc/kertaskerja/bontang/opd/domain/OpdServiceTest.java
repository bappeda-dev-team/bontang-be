package cc.kertaskerja.bontang.opd.domain;

import cc.kertaskerja.bontang.opd.domain.exception.OpdAlreadyExistException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdDeleteForbiddenException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdNotFoundException;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanRepository;
import cc.kertaskerja.bontang.pegawai.domain.PegawaiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
public class OpdServiceTest {

    @Mock
    private OpdRepository opdRepository;

    @Mock
    private PegawaiRepository pegawaiRepository;

    @Mock
    private BidangUrusanRepository bidangUrusanRepository;

    private OpdService opdService;

    @BeforeEach
    void setUp() {
        opdService = new OpdService(opdRepository, pegawaiRepository, bidangUrusanRepository);
    }

    @Test
    void findAll_returnsAllOpd() {
        Opd opd1 = new Opd(1L, "OPD-001", "BAPPEDA", Instant.now(), Instant.now());
        Opd opd2 = new Opd(2L, "OPD-002", "BPKAD", Instant.now(), Instant.now());
        List<Opd> opdList = Arrays.asList(opd1, opd2);

        when(opdRepository.findAll()).thenReturn(opdList);

        Iterable<Opd> result = opdService.findAll();

        assertEquals(opdList, result);
        verify(opdRepository).findAll();
    }

    @Test
    void detailOpdByKodeOpd_returnsOpd_whenFound() {
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());
        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.of(opd));

        Opd result = opdService.detailOpdByKodeOpd(kodeOpd);

        assertEquals(opd, result);
        verify(opdRepository).findByKodeOpd(kodeOpd);
    }

    @Test
    void detailOpdByKodeOpd_throwsException_whenNotFound() {
        String kodeOpd = "OPD-404";
        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.empty());

        assertThrows(OpdNotFoundException.class, () -> opdService.detailOpdByKodeOpd(kodeOpd));
        verify(opdRepository).findByKodeOpd(kodeOpd);
    }

    @Test
    void tambahOpd_savesOpd_whenKodeOpdNotExists() {
        String kodeOpd = "OPD-001";
        Opd opd = Opd.of(kodeOpd, "BAPPEDA");
        Opd savedOpd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());

        when(opdRepository.existsByKodeOpd(kodeOpd)).thenReturn(false);
        when(opdRepository.save(opd)).thenReturn(savedOpd);

        Opd result = opdService.tambahOpd(opd);

        assertEquals(savedOpd, result);
        verify(opdRepository).existsByKodeOpd(kodeOpd);
        verify(opdRepository).save(opd);
    }

    @Test
    void tambahOpd_throwsException_whenKodeOpdAlreadyExists() {
        String kodeOpd = "OPD-001";
        Opd opd = Opd.of(kodeOpd, "BAPPEDA");

        when(opdRepository.existsByKodeOpd(kodeOpd)).thenReturn(true);

        assertThrows(OpdAlreadyExistException.class, () -> opdService.tambahOpd(opd));
        verify(opdRepository).existsByKodeOpd(kodeOpd);
        verify(opdRepository, never()).save(any());
    }

    @Test
    void ubahOpd_savesOpd_whenKodeOpdExists() {
        String kodeOpd = "OPD-001";
        Opd opd = Opd.of(kodeOpd, "BAPPEDA");
        Opd updatedOpd = new Opd(1L, kodeOpd, "BAPPEDA UPDATED", Instant.now(), Instant.now());

        when(opdRepository.existsByKodeOpd(kodeOpd)).thenReturn(true);
        when(opdRepository.save(opd)).thenReturn(updatedOpd);

        Opd result = opdService.ubahOpd(kodeOpd, opd);

        assertEquals(updatedOpd, result);
        verify(opdRepository).existsByKodeOpd(kodeOpd);
        verify(opdRepository).save(opd);
    }

    @Test
    void ubahOpd_throwsException_whenKodeOpdNotExists() {
        String kodeOpd = "OPD-404";
        Opd opd = Opd.of(kodeOpd, "BAPPEDA");

        when(opdRepository.existsByKodeOpd(kodeOpd)).thenReturn(false);

        assertThrows(OpdNotFoundException.class, () -> opdService.ubahOpd(kodeOpd, opd));
        verify(opdRepository).existsByKodeOpd(kodeOpd);
        verify(opdRepository, never()).save(any());
    }

    @Test
    void hapusOpd_deletesOpd_whenKodeOpdExistsAndNoPegawai() {
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());

        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.of(opd));
        when(bidangUrusanRepository.existsByKodeOpd(kodeOpd)).thenReturn(false);
        when(pegawaiRepository.existsByOpdId(opd.id())).thenReturn(false);

        opdService.hapusOpd(kodeOpd);

        verify(opdRepository).findByKodeOpd(kodeOpd);
        verify(bidangUrusanRepository).existsByKodeOpd(kodeOpd);
        verify(pegawaiRepository).existsByOpdId(opd.id());
        verify(opdRepository).deleteByKodeOpd(kodeOpd);
    }

    @Test
    void hapusOpd_checksBidangBeforePegawai() {
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());

        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.of(opd));
        when(bidangUrusanRepository.existsByKodeOpd(kodeOpd)).thenReturn(false);
        when(pegawaiRepository.existsByOpdId(opd.id())).thenReturn(false);

        opdService.hapusOpd(kodeOpd);

        InOrder inOrder = inOrder(bidangUrusanRepository, pegawaiRepository);
        inOrder.verify(bidangUrusanRepository).existsByKodeOpd(kodeOpd);
        inOrder.verify(pegawaiRepository).existsByOpdId(opd.id());
    }

    @Test
    void hapusOpd_throwsException_whenKodeOpdNotExists() {
        String kodeOpd = "OPD-404";

        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.empty());

        assertThrows(OpdNotFoundException.class, () -> opdService.hapusOpd(kodeOpd));
        verify(opdRepository).findByKodeOpd(kodeOpd);
        verify(bidangUrusanRepository, never()).existsByKodeOpd(any());
        verify(pegawaiRepository, never()).existsByOpdId(any());
        verify(opdRepository, never()).deleteByKodeOpd(any());
    }

    @Test
    void hapusOpd_throwsException_whenPegawaiExists() {
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());

        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.of(opd));
        when(bidangUrusanRepository.existsByKodeOpd(kodeOpd)).thenReturn(false);
        when(pegawaiRepository.existsByOpdId(opd.id())).thenReturn(true);

        assertThrows(OpdDeleteForbiddenException.class, () -> opdService.hapusOpd(kodeOpd));
        verify(opdRepository).findByKodeOpd(kodeOpd);
        verify(bidangUrusanRepository).existsByKodeOpd(kodeOpd);
        verify(pegawaiRepository).existsByOpdId(opd.id());
        verify(opdRepository, never()).deleteByKodeOpd(any());
    }

    @Test
    void hapusOpd_throwsException_whenBidangUrusanExists() {
        String kodeOpd = "OPD-001";
        Opd opd = new Opd(1L, kodeOpd, "BAPPEDA", Instant.now(), Instant.now());

        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.of(opd));
        when(bidangUrusanRepository.existsByKodeOpd(kodeOpd)).thenReturn(true);

        assertThrows(OpdDeleteForbiddenException.class, () -> opdService.hapusOpd(kodeOpd));

        verify(opdRepository).findByKodeOpd(kodeOpd);
        verify(bidangUrusanRepository).existsByKodeOpd(kodeOpd);
        verify(pegawaiRepository, never()).existsByOpdId(any());
        verify(opdRepository, never()).deleteByKodeOpd(any());
    }
}
