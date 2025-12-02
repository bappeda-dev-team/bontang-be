package cc.kertaskerja.bontang.kegiatan.domain;

import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanAlreadyExistException;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanDeleteForbiddenException;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanNotFoundException;
import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.program.domain.ProgramRepository;
import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KegiatanService{
    private final KegiatanRepository kegiatanRepository;
    private final ProgramRepository programRepository;
    private final SubKegiatanRepository subKegiatanRepository;

    public KegiatanService(
            KegiatanRepository kegiatanRepository,
            ProgramRepository programRepository,
            SubKegiatanRepository subKegiatanRepository
    ) {
        this.kegiatanRepository = kegiatanRepository;
        this.programRepository = programRepository;
        this.subKegiatanRepository = subKegiatanRepository;
    }

    public Iterable<Kegiatan> findAll() {
        return kegiatanRepository.findAll();
    }

    public List<Kegiatan> findAllByKodeOpd(String kodeOpd) {
        return kegiatanRepository.findAllByKodeOpd(kodeOpd);
    }

    public Kegiatan detailKegiatanByKodeKegiatan(String kodeKegiatan) {
        return kegiatanRepository.findByKodeKegiatan(kodeKegiatan)
                .orElseThrow(() -> new KegiatanNotFoundException(kodeKegiatan));
    }

    public List<Kegiatan> detailKegiatanByKodeKegiatanIn(List<String> kodeKegiatan) {
        List<Kegiatan> kegiatans = kegiatanRepository.findAllByKodeKegiatanIn(kodeKegiatan);
        if (kegiatans.size() != kodeKegiatan.size()) {
            Set<String> foundKode = kegiatans.stream()
                    .map(Kegiatan::kodeKegiatan)
                    .collect(Collectors.toSet());

            String missingKode = kodeKegiatan.stream()
                    .filter(kode -> !foundKode.contains(kode))
                    .findFirst()
                    .orElse(null);

            if (missingKode != null) {
                throw new KegiatanNotFoundException(missingKode);
            }
        }

        return kegiatans;
    }

    public List<Kegiatan> detailKegiatanByKodeOpdAndKodeKegiatanIn(String kodeOpd, List<String> kodeKegiatan) {
        List<Kegiatan> kegiatans = kegiatanRepository.findAllByKodeKegiatanInAndKodeOpd(kodeKegiatan, kodeOpd);
        if (kegiatans.size() != kodeKegiatan.size()) {
            Set<String> foundKode = kegiatans.stream()
                    .map(Kegiatan::kodeKegiatan)
                    .collect(Collectors.toSet());

            String missingKode = kodeKegiatan.stream()
                    .filter(kode -> !foundKode.contains(kode))
                    .findFirst()
                    .orElse(null);

            if (missingKode != null) {
                throw new KegiatanNotFoundException(missingKode);
            }
        }

        return kegiatans;
    }

    public Kegiatan tambahKegiatan(Kegiatan kegiatan, String kodeProgram) {
        String kodeKegiatan = kegiatan.kodeKegiatan();
        if (kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)) {
            throw new KegiatanAlreadyExistException(kodeKegiatan);
        }

        Long programId = getProgramId(kodeProgram);
        Kegiatan kegiatanWithProgram = attachProgram(kegiatan, programId);
        return kegiatanRepository.save(kegiatanWithProgram);
    }

    public Kegiatan ubahKegiatan(String kodeKegiatan, Kegiatan kegiatan, String kodeProgram) {
        if (!kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)) {
            throw new KegiatanNotFoundException(kodeKegiatan);
        }

        Long programId = getProgramId(kodeProgram);
        Kegiatan kegiatanWithProgram = attachProgram(kegiatan, programId);
        return kegiatanRepository.save(kegiatanWithProgram);
    }

    public void hapusKegiatan(String kodeKegiatan) {
        Kegiatan kegiatan = kegiatanRepository.findByKodeKegiatan(kodeKegiatan)
                .orElseThrow(() -> new KegiatanNotFoundException(kodeKegiatan));

        if (subKegiatanRepository.existsByKegiatanId(kegiatan.id())) {
            throw new KegiatanDeleteForbiddenException(kodeKegiatan);
        }

        kegiatanRepository.deleteByKodeKegiatan(kodeKegiatan);
    }

    public void hapusKegiatan(String kodeOpd, String kodeKegiatan) {
        Kegiatan kegiatan = kegiatanRepository.findByKodeKegiatanAndKodeOpd(kodeKegiatan, kodeOpd)
                .orElseThrow(() -> new KegiatanNotFoundException(kodeKegiatan));

        if (subKegiatanRepository.existsByKegiatanId(kegiatan.id())) {
            throw new KegiatanDeleteForbiddenException(kodeKegiatan);
        }

        kegiatanRepository.deleteByKodeKegiatanAndKodeOpd(kodeKegiatan, kodeOpd);
    }

    public String getKodeProgram(Long programId) {
        return programRepository.findById(programId)
                .map(Program::kodeProgram)
                .orElseThrow(() -> new ProgramNotFoundException(String.valueOf(programId)));
    }

    private Long getProgramId(String kodeProgram) {
        return programRepository.findByKodeProgram(kodeProgram)
                .map(Program::id)
                .orElseThrow(() -> new ProgramNotFoundException(kodeProgram));
    }

    private Kegiatan attachProgram(Kegiatan kegiatan, Long programId) {
        return new Kegiatan(
                kegiatan.id(),
                kegiatan.kodeKegiatan(),
                kegiatan.namaKegiatan(),
                programId,
                kegiatan.createdDate(),
                kegiatan.lastModifiedDate()
        );
    }
}
