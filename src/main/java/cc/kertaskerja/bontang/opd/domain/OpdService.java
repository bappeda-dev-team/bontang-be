package cc.kertaskerja.bontang.opd.domain;

import cc.kertaskerja.bontang.opd.domain.exception.OpdAlreadyExistException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdDeleteForbiddenException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdNotFoundException;
import cc.kertaskerja.bontang.pegawai.domain.PegawaiRepository;
import org.springframework.stereotype.Service;

@Service
public class OpdService {
    private final OpdRepository opdRepository;
    private final PegawaiRepository pegawaiRepository;

    public OpdService(OpdRepository opdRepository, PegawaiRepository pegawaiRepository) {
        this.opdRepository = opdRepository;
        this.pegawaiRepository = pegawaiRepository;
    }

    public Iterable<Opd> findAll() {
        return opdRepository.findAll();
    }

    public Opd detailOpdByKodeOpd(String kodeOpd) {
        return opdRepository.findByKodeOpd(kodeOpd)
                .orElseThrow(() -> new OpdNotFoundException(kodeOpd));
    }

    public Opd tambahOpd(Opd opd) {
        String kodeOpd = opd.kodeOpd();
        if (opdRepository.existsByKodeOpd(kodeOpd)) {
            throw new OpdAlreadyExistException(kodeOpd);
        }

        return opdRepository.save(opd);
    }

    public Opd ubahOpd(String kodeOpd, Opd opd) {
        if (!opdRepository.existsByKodeOpd(kodeOpd)) {
            throw new OpdNotFoundException(kodeOpd);
        }

        return opdRepository.save(opd);
    }

    public void hapusOpd(String kodeOpd) {
        Opd opd = opdRepository.findByKodeOpd(kodeOpd)
                .orElseThrow(() -> new OpdNotFoundException(kodeOpd));

        if (pegawaiRepository.existsByOpdId(opd.id())) {
            throw new OpdDeleteForbiddenException(kodeOpd);
        }

        opdRepository.deleteByKodeOpd(kodeOpd);
    }
}
