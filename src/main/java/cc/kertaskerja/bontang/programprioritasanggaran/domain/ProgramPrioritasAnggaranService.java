package cc.kertaskerja.bontang.programprioritasanggaran.domain;

import cc.kertaskerja.bontang.programprioritas.domain.ProgramPrioritasRepository;
import cc.kertaskerja.bontang.programprioritas.domain.exception.ProgramPrioritasNotFoundException;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranNotFoundException;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException;
import cc.kertaskerja.bontang.programprioritasanggaran.web.CheckRelasiProgramPrioritasResponse;
import cc.kertaskerja.bontang.programprioritasanggaran.web.CheckRelasiRencanaKinerjaResponse;
import cc.kertaskerja.bontang.programprioritasanggaran.web.ProgramPrioritasAnggaranRequest;
import cc.kertaskerja.bontang.programprioritasanggaran.web.ProgramPrioritasAnggaranRencanaKinerjaResponse;
import cc.kertaskerja.bontang.programprioritasanggaran.web.ProgramPrioritasAnggaranWithRencanaKinerjaResponse;
import cc.kertaskerja.bontang.programprioritasanggaran.web.RencanaKinerjaBatchRequest;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProgramPrioritasAnggaranService {
    private final ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository;
    private final ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository;
    private final RencanaKinerjaRepository rencanaKinerjaEntityRepository;
    private final ProgramPrioritasRepository programPrioritasRepository;

    public ProgramPrioritasAnggaranService(
            ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository,
            ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository,
            RencanaKinerjaRepository rencanaKinerjaEntityRepository,
            ProgramPrioritasRepository programPrioritasRepository
    ) {
        this.programPrioritasAnggaranRepository = programPrioritasAnggaranRepository;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.rencanaKinerjaEntityRepository = rencanaKinerjaEntityRepository;
        this.programPrioritasRepository = programPrioritasRepository;
    }

    public Iterable<ProgramPrioritasAnggaran> findAll() {
        return programPrioritasAnggaranRepository.findAll();
    }

    public List<ProgramPrioritasAnggaranWithRencanaKinerjaResponse> getByKodeOpdNipTahun(String kodeOpd, String nip, Integer tahun) {
        Iterable<ProgramPrioritasAnggaran> programs = programPrioritasAnggaranRepository.findByKodeOpdAndNipAndTahun(kodeOpd, nip, tahun);
        List<ProgramPrioritasAnggaranWithRencanaKinerjaResponse> result = new ArrayList<>();

        for (ProgramPrioritasAnggaran program : programs) {
            List<ProgramPrioritasAnggaranRencanaKinerja> rencanaKinerjaList =
                    rencanaKinerjaRepository.findByIdProgramPrioritasAnggaran(program.id());

            List<ProgramPrioritasAnggaranWithRencanaKinerjaResponse.RencanaKinerjaItem> rencanaKinerjaItems = rencanaKinerjaList.stream()
                    .map(rk -> {
                        Optional<RencanaKinerja> rencanaKinerja = rencanaKinerjaEntityRepository.findById(rk.idRencanaKinerja());
                        String namaRencanaKinerja = rencanaKinerja.map(RencanaKinerja::rencanaKinerja).orElse(null);
                        return new ProgramPrioritasAnggaranWithRencanaKinerjaResponse.RencanaKinerjaItem(
                                rk.idRencanaKinerja(),
                                namaRencanaKinerja
                        );
                    })
                    .collect(Collectors.toList());

            result.add(new ProgramPrioritasAnggaranWithRencanaKinerjaResponse(
                    program.id(),
                    program.idProgramPrioritas(),
                    program.kodeOpd(),
                    program.nip(),
                    program.tahun(),
                    rencanaKinerjaItems,
                    program.createdDate(),
                    program.lastModifiedDate()
            ));
        }

        return result;
    }

    public ProgramPrioritasAnggaran detailProgramPrioritasAnggaranById(Long id) {
        return programPrioritasAnggaranRepository.findById(id)
                .orElseThrow(() -> new ProgramPrioritasAnggaranNotFoundException(id));
    }

    @Transactional
    public ProgramPrioritasAnggaran simpanPerencanaanAnggaran(ProgramPrioritasAnggaranRequest request) {
        return programPrioritasAnggaranRepository.save(buildProgramPrioritasAnggaran(request));
    }

    @Transactional
    public List<ProgramPrioritasAnggaran> simpanPerencanaanAnggaran(List<ProgramPrioritasAnggaranRequest> requests) {
        return requests.stream()
                .map(this::buildProgramPrioritasAnggaran)
                .map(programPrioritasAnggaranRepository::save)
                .collect(Collectors.toList());
    }

    private ProgramPrioritasAnggaran buildProgramPrioritasAnggaran(ProgramPrioritasAnggaranRequest request) {
        return ProgramPrioritasAnggaran.of(
                request.idProgramPrioritas(),
                request.kodeOpd(),
                request.nip(),
                request.tahun()
        );
    }

    @Transactional
    public ProgramPrioritasAnggaran ubahPerencanaanAnggaran(Long id, ProgramPrioritasAnggaranRequest request) {
        ProgramPrioritasAnggaran existing = detailProgramPrioritasAnggaranById(id);

        ProgramPrioritasAnggaran updated = new ProgramPrioritasAnggaran(
                existing.id(),
                request.idProgramPrioritas(),
                request.kodeOpd(),
                request.nip(),
                request.tahun(),
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

        Set<Long> requestedIds = rencanaKinerjaItems.stream()
                .map(RencanaKinerjaBatchRequest.RencanaKinerjaItem::getIdRencanaKinerja)
                .collect(Collectors.toSet());

        List<ProgramPrioritasAnggaranRencanaKinerja> existingRelations =
                rencanaKinerjaRepository.findByIdProgramPrioritasAnggaran(idProgramPrioritasAnggaran);

        // Hapus data relasi yang tidak ada di dalam request
        for (ProgramPrioritasAnggaranRencanaKinerja existing : existingRelations) {
            if (!requestedIds.contains(existing.idRencanaKinerja())) {
                rencanaKinerjaRepository.deleteByIdProgramPrioritasAnggaranAndIdRencanaKinerja(
                        idProgramPrioritasAnggaran,
                        existing.idRencanaKinerja()
                );
            }
        }

        // UPSERT
        for (RencanaKinerjaBatchRequest.RencanaKinerjaItem item : rencanaKinerjaItems) {
            Optional<ProgramPrioritasAnggaranRencanaKinerja> existingRelasi =
                    rencanaKinerjaRepository.findByIdProgramPrioritasAnggaranAndIdRencanaKinerja(
                            idProgramPrioritasAnggaran, item.getIdRencanaKinerja());

            ProgramPrioritasAnggaranRencanaKinerja relasi;

            if (existingRelasi.isPresent()) {
                // Relasi ditemukan
                ProgramPrioritasAnggaranRencanaKinerja existing = existingRelasi.get();
                relasi = rencanaKinerjaRepository.save(
                        new ProgramPrioritasAnggaranRencanaKinerja(
                                existing.id(),
                                existing.idProgramPrioritasAnggaran(),
                                existing.idRencanaKinerja(),
                                existing.createdDate(),
                                null
                        )
                );
            } else {
                // Buat relasi baru
                ProgramPrioritasAnggaranRencanaKinerja newRelasi = ProgramPrioritasAnggaranRencanaKinerja.of(
                        idProgramPrioritasAnggaran,
                        item.getIdRencanaKinerja()
                );
                relasi = rencanaKinerjaRepository.save(newRelasi);
            }

            // Ambil nama rencana kinerja dari entity RencanaKinerja
            Optional<RencanaKinerja> rencanaKinerja = rencanaKinerjaEntityRepository.findById(item.getIdRencanaKinerja());
            String namaRencanaKinerja = rencanaKinerja.map(RencanaKinerja::rencanaKinerja).orElse(null);

            result.add(new ProgramPrioritasAnggaranRencanaKinerjaResponse(
                    relasi.id(),
                    relasi.idProgramPrioritasAnggaran(),
                    relasi.idRencanaKinerja(),
                    namaRencanaKinerja,
                    relasi.createdDate(),
                    relasi.lastModifiedDate()
            ));
        }

        return result;
    }

    public CheckRelasiProgramPrioritasResponse checkRelasiByProgramPrioritas(Long idProgramPrioritas) {
        // Validasi apakah program prioritas ada
        if (!programPrioritasRepository.existsById(idProgramPrioritas)) {
            throw new ProgramPrioritasNotFoundException(idProgramPrioritas);
        }

        // Hitung jumlah relasi
        long count = programPrioritasAnggaranRepository.countByIdProgramPrioritas(idProgramPrioritas);

        return CheckRelasiProgramPrioritasResponse.of(idProgramPrioritas, count);
    }

    public CheckRelasiRencanaKinerjaResponse checkRelasiByRencanaKinerja(Long idRencanaKinerja) {
        // Validasi apakah rencana kinerja ada
        if (!rencanaKinerjaEntityRepository.existsById(idRencanaKinerja)) {
            throw new RencanaKinerjaNotFoundException(idRencanaKinerja);
        }

        // Hitung jumlah relasi
        long count = rencanaKinerjaRepository.countByIdRencanaKinerja(idRencanaKinerja);

        return CheckRelasiRencanaKinerjaResponse.of(idRencanaKinerja, count);
    }
}
