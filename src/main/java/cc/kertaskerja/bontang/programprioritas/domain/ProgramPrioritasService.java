package cc.kertaskerja.bontang.programprioritas.domain;

import cc.kertaskerja.bontang.programprioritas.domain.exception.ProgramPrioritasNotFoundException;
import cc.kertaskerja.bontang.programprioritas.web.ProgramPrioritasRequest;
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

    

    public Iterable<ProgramPrioritas> findByPeriodeTahunRange(Integer periodeTahunAwal, Integer periodeTahunAkhir) {
        return programPrioritasRepository.findByPeriodeTahunAwalGreaterThanEqualAndPeriodeTahunAkhirLessThanEqual(
                periodeTahunAwal,
                periodeTahunAkhir
        );
    }

    public ProgramPrioritas tambahProgramPrioritas(ProgramPrioritas programPrioritas) {
        return programPrioritasRepository.save(programPrioritas);
    }

    public ProgramPrioritas tambahProgramPrioritas(ProgramPrioritasRequest request) {
        ProgramPrioritas programPrioritas = ProgramPrioritas.of(
                request.programPrioritas(),
                request.tahun(),
                request.keterangan(),
                request.periodeTahunAwal(),
                request.periodeTahunAkhir(),
                request.status()
        );

        return tambahProgramPrioritas(programPrioritas);
    }

    public ProgramPrioritas ubahProgramPrioritas(Long id, ProgramPrioritas programPrioritas) {
        if (!programPrioritasRepository.existsById(id)) {
            throw new ProgramPrioritasNotFoundException(id);
        }

        return programPrioritasRepository.save(programPrioritas);
    }

    public ProgramPrioritas ubahProgramPrioritas(Long id, ProgramPrioritasRequest request) {
        ProgramPrioritas existingProgramPrioritas = detailProgramPrioritasById(id);

        ProgramPrioritas programPrioritas = new ProgramPrioritas(
                existingProgramPrioritas.id(),
                request.programPrioritas(),
                request.tahun(),
                request.keterangan(),
                request.periodeTahunAwal(),
                request.periodeTahunAkhir(),
                request.status(),
                existingProgramPrioritas.createdDate(),
                null
        );

        return ubahProgramPrioritas(id, programPrioritas);
    }

    public void hapusProgramPrioritas(Long id) {
        if (!programPrioritasRepository.existsById(id)) {
            throw new ProgramPrioritasNotFoundException(id);
        }

        programPrioritasRepository.deleteById(id);
    }
}
