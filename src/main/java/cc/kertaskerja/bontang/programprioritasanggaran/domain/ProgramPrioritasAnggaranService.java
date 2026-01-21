package cc.kertaskerja.bontang.programprioritasanggaran.domain;

import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranNotFoundException;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException;
import cc.kertaskerja.bontang.programprioritasanggaran.web.ProgramPrioritasAnggaranRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProgramPrioritasAnggaranService {
    private final ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository;
    private final ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository;

    public ProgramPrioritasAnggaranService(
            ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository,
            ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository
    ) {
        this.programPrioritasAnggaranRepository = programPrioritasAnggaranRepository;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
    }

    public Iterable<ProgramPrioritasAnggaran> findAll() {
        return programPrioritasAnggaranRepository.findAll();
    }

    public ProgramPrioritasAnggaran detailProgramPrioritasAnggaranById(Long id) {
        return programPrioritasAnggaranRepository.findById(id)
                .orElseThrow(() -> new ProgramPrioritasAnggaranNotFoundException(id));
    }

    @Transactional
    public ProgramPrioritasAnggaran simpanPerencanaanAnggaran(ProgramPrioritasAnggaranRequest request) {
        ProgramPrioritasAnggaran programPrioritasAnggaran = ProgramPrioritasAnggaran.of(
                request.idProgramPrioritas(),
                request.kodeOpd()
        );

        ProgramPrioritasAnggaran saved = programPrioritasAnggaranRepository.save(programPrioritasAnggaran);

        if (request.idRencanaKinerjaList() != null && !request.idRencanaKinerjaList().isEmpty()) {
            simpanAtauPerbaruiListRencanaKinerja(saved.id(), request.idRencanaKinerjaList());
        }

        return saved;
    }

    @Transactional
    public ProgramPrioritasAnggaran ubahPerencanaanAnggaran(Long id, ProgramPrioritasAnggaranRequest request) {
        ProgramPrioritasAnggaran existing = detailProgramPrioritasAnggaranById(id);

        ProgramPrioritasAnggaran updated = new ProgramPrioritasAnggaran(
                existing.id(),
                request.idProgramPrioritas(),
                request.kodeOpd(),
                existing.createdDate(),
                null
        );

        ProgramPrioritasAnggaran saved = programPrioritasAnggaranRepository.save(updated);

        if (request.idRencanaKinerjaList() != null) {
            simpanAtauPerbaruiListRencanaKinerja(saved.id(), request.idRencanaKinerjaList());
        }

        return saved;
    }

    @Transactional
    public void simpanAtauPerbaruiListRencanaKinerja(Long idProgramPrioritasAnggaran, List<Long> idRencanaKinerjaList) {
        if (!programPrioritasAnggaranRepository.existsById(idProgramPrioritasAnggaran)) {
            throw new ProgramPrioritasAnggaranNotFoundException(idProgramPrioritasAnggaran);
        }

        rencanaKinerjaRepository.deleteByIdProgramPrioritasAnggaran(idProgramPrioritasAnggaran);

        if (idRencanaKinerjaList != null && !idRencanaKinerjaList.isEmpty()) {
            for (Long idRencanaKinerja : idRencanaKinerjaList) {
                ProgramPrioritasAnggaranRencanaKinerja relasi = ProgramPrioritasAnggaranRencanaKinerja.of(
                        idProgramPrioritasAnggaran,
                        idRencanaKinerja
                );
                rencanaKinerjaRepository.save(relasi);
            }
        }
    }

    public Iterable<ProgramPrioritasAnggaranRencanaKinerja> getListRencanaKinerja(Long idProgramPrioritasAnggaran) {
        if (!programPrioritasAnggaranRepository.existsById(idProgramPrioritasAnggaran)) {
            throw new ProgramPrioritasAnggaranNotFoundException(idProgramPrioritasAnggaran);
        }
        return rencanaKinerjaRepository.findByIdProgramPrioritasAnggaran(idProgramPrioritasAnggaran);
    }

    @Transactional
    public ProgramPrioritasAnggaranRencanaKinerja addRencanaKinerja(
            Long idProgramPrioritasAnggaran,
            Long idRencanaKinerja
    ) {
        if (!programPrioritasAnggaranRepository.existsById(idProgramPrioritasAnggaran)) {
            throw new ProgramPrioritasAnggaranNotFoundException(idProgramPrioritasAnggaran);
        }

        if (rencanaKinerjaRepository.existsByIdProgramPrioritasAnggaranAndIdRencanaKinerja(
                idProgramPrioritasAnggaran, idRencanaKinerja)) {
            throw new ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException(
                    idRencanaKinerja, idProgramPrioritasAnggaran);
        }

        ProgramPrioritasAnggaranRencanaKinerja relasi = ProgramPrioritasAnggaranRencanaKinerja.of(
                idProgramPrioritasAnggaran,
                idRencanaKinerja
        );

        return rencanaKinerjaRepository.save(relasi);
    }

    @Transactional
    public void removeRencanaKinerja(Long idProgramPrioritasAnggaran, Long idRencanaKinerja) {
        if (!programPrioritasAnggaranRepository.existsById(idProgramPrioritasAnggaran)) {
            throw new ProgramPrioritasAnggaranNotFoundException(idProgramPrioritasAnggaran);
        }

        rencanaKinerjaRepository.deleteByIdProgramPrioritasAnggaranAndIdRencanaKinerja(
                idProgramPrioritasAnggaran, idRencanaKinerja);
    }

    public void hapusProgramPrioritasAnggaran(Long id) {
        if (!programPrioritasAnggaranRepository.existsById(id)) {
            throw new ProgramPrioritasAnggaranNotFoundException(id);
        }

        rencanaKinerjaRepository.deleteByIdProgramPrioritasAnggaran(id);
        programPrioritasAnggaranRepository.deleteById(id);
    }
}
