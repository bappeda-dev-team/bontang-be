package cc.kertaskerja.bontang.programprioritasanggaran.domain;

import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranNotFoundException;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException;
import cc.kertaskerja.bontang.programprioritasanggaran.web.ProgramPrioritasAnggaranRequest;
import cc.kertaskerja.bontang.programprioritasanggaran.web.ProgramPrioritasAnggaranRencanaKinerjaResponse;
import cc.kertaskerja.bontang.programprioritasanggaran.web.RencanaKinerjaBatchRequest;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramPrioritasAnggaranService {
    private final ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository;
    private final ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository;
    private final RencanaKinerjaRepository rencanaKinerjaEntityRepository;

    public ProgramPrioritasAnggaranService(
            ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository,
            ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository,
            RencanaKinerjaRepository rencanaKinerjaEntityRepository
    ) {
        this.programPrioritasAnggaranRepository = programPrioritasAnggaranRepository;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.rencanaKinerjaEntityRepository = rencanaKinerjaEntityRepository;
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

        return programPrioritasAnggaranRepository.save(programPrioritasAnggaran);
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

        return programPrioritasAnggaranRepository.save(updated);
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

    @Transactional
    public List<ProgramPrioritasAnggaranRencanaKinerjaResponse> addRencanaKinerjaBatch(
            Long idProgramPrioritasAnggaran,
            List<RencanaKinerjaBatchRequest.RencanaKinerjaItem> rencanaKinerjaItems
    ) {
        if (!programPrioritasAnggaranRepository.existsById(idProgramPrioritasAnggaran)) {
            throw new ProgramPrioritasAnggaranNotFoundException(idProgramPrioritasAnggaran);
        }

        List<ProgramPrioritasAnggaranRencanaKinerjaResponse> result = new ArrayList<>();

        for (RencanaKinerjaBatchRequest.RencanaKinerjaItem item : rencanaKinerjaItems) {
            if (rencanaKinerjaRepository.existsByIdProgramPrioritasAnggaranAndIdRencanaKinerja(
                    idProgramPrioritasAnggaran, item.getIdRencanaKinerja())) {
                throw new ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException(
                        item.getIdRencanaKinerja(), idProgramPrioritasAnggaran);
            }

            ProgramPrioritasAnggaranRencanaKinerja relasi = ProgramPrioritasAnggaranRencanaKinerja.of(
                    idProgramPrioritasAnggaran,
                    item.getIdRencanaKinerja()
            );

            ProgramPrioritasAnggaranRencanaKinerja saved = rencanaKinerjaRepository.save(relasi);

            // Ambil nama rencana kinerja dari entity RencanaKinerja
            Optional<RencanaKinerja> rencanaKinerja = rencanaKinerjaEntityRepository.findById(item.getIdRencanaKinerja());
            String namaRencanaKinerja = rencanaKinerja.map(RencanaKinerja::rencanaKinerja).orElse(null);

            result.add(new ProgramPrioritasAnggaranRencanaKinerjaResponse(
                    saved.id(),
                    saved.idProgramPrioritasAnggaran(),
                    saved.idRencanaKinerja(),
                    namaRencanaKinerja,
                    saved.createdDate(),
                    saved.lastModifiedDate()
            ));
        }

        return result;
    }
}
