package cc.kertaskerja.bontang.programprioritasopd.domain;

import cc.kertaskerja.bontang.programprioritasopd.domain.exception.ProgramPrioritasOpdNotFoundException;
import cc.kertaskerja.bontang.programprioritasopd.web.ProgramPrioritasOpdRequest;
import org.springframework.stereotype.Service;

@Service
public class ProgramPrioritasOpdService {
    private ProgramPrioritasOpdRepository programPrioritasOpdRepository;

    public ProgramPrioritasOpdService(ProgramPrioritasOpdRepository programPrioritasOpdRepository) {
        this.programPrioritasOpdRepository = programPrioritasOpdRepository;
    }

    public Iterable<ProgramPrioritasOpd> findAll() {
        return programPrioritasOpdRepository.findAll();
    }

    public ProgramPrioritasOpd detailProgramPrioritasOpdById(Long id) {
        return programPrioritasOpdRepository.findById(id)
                .orElseThrow(() -> new ProgramPrioritasOpdNotFoundException(id));
    }
    
    public Iterable<ProgramPrioritasOpd> getByIdProgramPrioritas(Long idProgramPrioritas) {
        return programPrioritasOpdRepository.findByIdProgramPrioritas(idProgramPrioritas);
    }

    public Iterable<ProgramPrioritasOpd> getByIdSubkegiatanOpd(Long idSubkegiatanOpd) {
        return programPrioritasOpdRepository.findByIdSubkegiatanOpd(idSubkegiatanOpd);
    }

    public Iterable<ProgramPrioritasOpd> getByIdRencanaKinerja(Long idRencanaKinerja) {
        return programPrioritasOpdRepository.findByIdRencanaKinerja(idRencanaKinerja);
    }

    public ProgramPrioritasOpd tambahProgramPrioritasOpd(ProgramPrioritasOpd programPrioritasOpd) {
        return programPrioritasOpdRepository.save(programPrioritasOpd);
    }

    public ProgramPrioritasOpd tambahProgramPrioritasOpd(ProgramPrioritasOpdRequest request) {
        ProgramPrioritasOpd programPrioritasOpd = ProgramPrioritasOpd.of(
                request.idSubkegiatanOpd(),
                request.idRencanaKinerja(),
                request.idProgramPrioritas(),
                request.tahun(),
                request.kodeOpd(),
                request.status(),
                request.keterangan()
        );

        return tambahProgramPrioritasOpd(programPrioritasOpd);
    }

    public ProgramPrioritasOpd ubahProgramPrioritasOpd(Long id, ProgramPrioritasOpd programPrioritasOpd) {
        if (!programPrioritasOpdRepository.existsById(id)) {
            throw new ProgramPrioritasOpdNotFoundException(id);
        }

        return programPrioritasOpdRepository.save(programPrioritasOpd);
    }

    public ProgramPrioritasOpd ubahProgramPrioritasOpd(Long id, ProgramPrioritasOpdRequest request) {
        ProgramPrioritasOpd existingProgramPrioritasOpd = detailProgramPrioritasOpdById(id);

        ProgramPrioritasOpd programPrioritasOpd = new ProgramPrioritasOpd(
                existingProgramPrioritasOpd.id(),
                request.idSubkegiatanOpd(),
                request.idRencanaKinerja(),
                request.idProgramPrioritas(),
                request.tahun(),
                request.kodeOpd(),
                request.status(),
                request.keterangan(),
                existingProgramPrioritasOpd.createdDate(),
                null
        );

        return ubahProgramPrioritasOpd(id, programPrioritasOpd);
    }

    public void hapusProgramPrioritasOpd(Long id) {
        if (!programPrioritasOpdRepository.existsById(id)) {
            throw new ProgramPrioritasOpdNotFoundException(id);
        }

        programPrioritasOpdRepository.deleteById(id);
    }
}
