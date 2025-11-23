package cc.kertaskerja.bontang.program.domain;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;

@Service
public class ProgramService {
    private ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public Iterable<Program> findAll() {
        return programRepository.findAll();
    }

    public Program detailProgramByKodeProgram(String kodeProgram) {
        return programRepository.findByKodeProgram(kodeProgram)
                .orElseThrow(() -> new ProgramNotFoundException(kodeProgram));
    }

    public Program tambahProgram(Program program) {

        return programRepository.save(program);
    }

    public Program ubahProgram(String kodeProgram, Program program) {
        if (!programRepository.existsByKodeProgram(kodeProgram)) {
            throw new ProgramNotFoundException(kodeProgram);
        }

        return programRepository.save(program);
    }

    public void hapusProgram(String kodeProgram) {
        if (!programRepository.existsByKodeProgram(kodeProgram)) {
            throw new ProgramNotFoundException(kodeProgram);
        }

        programRepository.deleteByKodeProgram(kodeProgram);
    }
}
