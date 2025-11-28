package cc.kertaskerja.bontang.pegawai.domain;

import cc.kertaskerja.bontang.opd.domain.Opd;
import cc.kertaskerja.bontang.opd.domain.OpdRepository;
import cc.kertaskerja.bontang.opd.domain.exception.OpdNotFoundException;
import cc.kertaskerja.bontang.pegawai.domain.exception.PegawaiNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PegawaiService {
    public final PegawaiRepository pegawaiRepository;
    private final OpdRepository opdRepository;

    public PegawaiService(PegawaiRepository pegawaiRepository, OpdRepository opdRepository) {
        this.pegawaiRepository = pegawaiRepository;
        this.opdRepository = opdRepository;
    }

    public Iterable<Pegawai> findAll() {
        return pegawaiRepository.findAll();
    }

    public Pegawai detailPegawaiByNip(String nip) {
        return pegawaiRepository.findByNip(nip)
                .orElseThrow(() -> new PegawaiNotFoundException(nip));
    }

    public Pegawai tambahPegawai(Pegawai pegawai, String kodeOpd) {
        Long opdId = getOpdId(kodeOpd);
        Pegawai pegawaiWithOpd = attachOpd(pegawai, opdId);
        return pegawaiRepository.save(pegawaiWithOpd);
    }

    public Pegawai ubahPegawai(String nip, Pegawai pegawai, String kodeOpd) {
        if (!pegawaiRepository.existsByNip(nip)) {
            throw new PegawaiNotFoundException(nip);
        }

        Long opdId = getOpdId(kodeOpd);
        Pegawai pegawaiWithOpd = attachOpd(pegawai, opdId);
        return pegawaiRepository.save(pegawaiWithOpd);
    }

    public void hapusPegawai(String nip) {
        if (!pegawaiRepository.existsByNip(nip)) {
            throw new PegawaiNotFoundException(nip);
        }

        pegawaiRepository.deleteByNip(nip);
    }

    private Long getOpdId(String kodeOpd) {
        return opdRepository.findByKodeOpd(kodeOpd)
                .map(Opd::id)
                .orElseThrow(() -> new OpdNotFoundException(kodeOpd));
    }

    private Pegawai attachOpd(Pegawai pegawai, Long opdId) {
        return new Pegawai(
                pegawai.id(),
                opdId,
                pegawai.namaPegawai(),
                pegawai.nip(),
                pegawai.email(),
                pegawai.jabatanDinas(),
                pegawai.jabatanTim(),
                pegawai.createdDate(),
                pegawai.lastModifiedDate()
        );
    }
}
