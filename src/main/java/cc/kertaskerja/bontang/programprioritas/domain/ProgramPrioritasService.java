package cc.kertaskerja.bontang.programprioritas.domain;

import cc.kertaskerja.bontang.programprioritas.domain.exception.ProgramPrioritasNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProgramPrioritasService{
    private ProgramPrioritasRepository programPrioritasRepository;

    public ProgramPrioritasService(ProgramPrioritasRepository programPrioritasRepository) {
        this.programPrioritasRepository = programPrioritasRepository;
    }

    public Iterable<ProgramPrioritas> findAll() {
        return programPrioritasRepository.findAll();
    }

    public ProgramPrioritas detailProgramPrioritasById(Long id) {
        return programPrioritasRepository.findById(id)
                .orElseThrow(() -> new ProgramPrioritasNotFoundException(id));
    }

    public ProgramPrioritas tambahProgramPrioritas(ProgramPrioritas programPrioritas) {
        return programPrioritasRepository.save(programPrioritas);
    }

    public ProgramPrioritas ubahProgramPrioritas(Long id, ProgramPrioritas programPrioritas) {
        if (!programPrioritasRepository.existsById(id)) {
            throw new ProgramPrioritasNotFoundException(id);
        }

        return programPrioritasRepository.save(programPrioritas);
    }

    public void hapusProgramPrioritas(Long id) {
        if (!programPrioritasRepository.existsById(id)) {
            throw new ProgramPrioritasNotFoundException(id);
        }

        programPrioritasRepository.deleteById(id);
    }
}
