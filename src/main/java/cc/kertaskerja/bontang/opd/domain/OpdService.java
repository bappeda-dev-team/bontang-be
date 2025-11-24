package cc.kertaskerja.bontang.opd.domain;

import cc.kertaskerja.bontang.opd.domain.exception.OpdAlreadyExistException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdDeleteForbiddenException;
import cc.kertaskerja.bontang.opd.domain.exception.OpdNotFoundException;
import cc.kertaskerja.bontang.program.domain.ProgramRepository;
import org.springframework.stereotype.Service;

@Service
public class OpdService {
    private OpdRepository opdRepository;
    private ProgramRepository programRepository;

    public OpdService(OpdRepository opdRepository, ProgramRepository programRepository) {
        this.opdRepository = opdRepository;
        this.programRepository = programRepository;
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

        boolean opdDigunakanPadaProgram = programRepository.existsByKodeOpd(kodeOpd);
        if (opdDigunakanPadaProgram) {
            throw new OpdDeleteForbiddenException(kodeOpd);
        }

        opdRepository.deleteByKodeOpd(kodeOpd);
    }
}
