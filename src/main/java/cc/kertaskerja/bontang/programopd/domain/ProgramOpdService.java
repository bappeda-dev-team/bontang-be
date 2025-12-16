package cc.kertaskerja.bontang.programopd.domain;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.programopd.domain.exception.ProgramOpdNotFoundException;

@Service
public class ProgramOpdService {
	private final ProgramOpdRepository programOpdRepository;

    public ProgramOpdService(ProgramOpdRepository programOpdRepository) {
        this.programOpdRepository = programOpdRepository;
    }

    public Iterable<ProgramOpd> findAll() {
        return programOpdRepository.findAll();
    }

    public ProgramOpd detailProgramOpdByKodeProgram(String kodeProgramOpd) {
        return programOpdRepository.findByKodeProgramOpd(kodeProgramOpd)
                .orElseThrow(() -> new ProgramOpdNotFoundException(kodeProgramOpd));
    }

    public ProgramOpd tambahProgramOpd(ProgramOpd programOpd) {

        return programOpdRepository.save(programOpd);
    }

    public ProgramOpd simpanProgramOpd(Program program) {
        ProgramOpd programOpd = programOpdRepository.findByKodeProgramOpd(program.kodeProgram())
                .map(existing -> new ProgramOpd(
                        existing.id(),
                        program.kodeProgram(),
                        program.namaProgram(),
                        program.kodeOpd(),
                        program.tahun(),
                        existing.createdDate(),
                        null
                ))
                .orElseGet(() -> ProgramOpd.of(
                        program.kodeProgram(),
                        program.namaProgram(),
                        program.kodeOpd(),
                        program.tahun()
                ));

        return programOpdRepository.save(programOpd);
    }

    public ProgramOpd ubahProgramOpd(String kodeProgramOpd, ProgramOpd programOpd) {
        if (!programOpdRepository.existsByKodeProgramOpd(kodeProgramOpd)) {
            throw new ProgramOpdNotFoundException(kodeProgramOpd);
        }

        return programOpdRepository.save(programOpd);
    }

    public void hapusProgramOpd(String kodeProgramOpd) {
        if (!programOpdRepository.existsByKodeProgramOpd(kodeProgramOpd)) {
            throw new ProgramOpdNotFoundException(kodeProgramOpd);
        }

        programOpdRepository.deleteByKodeProgramOpd(kodeProgramOpd);
    }
}
