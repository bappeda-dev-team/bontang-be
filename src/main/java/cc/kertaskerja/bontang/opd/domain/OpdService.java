package cc.kertaskerja.bontang.opd.domain;

import cc.kertaskerja.bontang.opd.domain.exception.OpdAlreadyExistException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OpdService {
    private OpdRepository opdRepository;

    public OpdService(OpdRepository opdRepository) {
        this.opdRepository = opdRepository;
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
        if (!opdRepository.existsByKodeOpd(kodeOpd)) {
            throw new OpdNotFoundException(kodeOpd);
        }

        opdRepository.deleteByKodeOpd(kodeOpd);
    }
}
