package cc.kertaskerja.bontang.kegiatan.domain;

import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanAlreadyExistException;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanDeleteForbiddenException;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanNotFoundException;
import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.program.domain.ProgramRepository;
import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatanRepository;
import org.springframework.stereotype.Service;

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

    public Kegiatan detailKegiatanByKodeKegiatan(String kodeKegiatan) {
        return kegiatanRepository.findByKodeKegiatan(kodeKegiatan)
                .orElseThrow(() -> new KegiatanNotFoundException(kodeKegiatan));
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
