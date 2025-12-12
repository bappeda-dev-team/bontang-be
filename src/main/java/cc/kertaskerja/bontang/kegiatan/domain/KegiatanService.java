package cc.kertaskerja.bontang.kegiatan.domain;

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

    public Kegiatan detailKegiatanByKodeKegiatan(String kodeKegiatan) {
        return kegiatanRepository.findByKodeKegiatan(kodeKegiatan)
                .orElseThrow(() -> new KegiatanNotFoundException(kodeKegiatan));
    }

    public Kegiatan tambahKegiatan(Kegiatan kegiatan) {
    	
        return kegiatanRepository.save(kegiatan);
    }

    public Kegiatan ubahKegiatan(String kodeKegiatan, Kegiatan kegiatan) {
        if (!kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)) {
            throw new KegiatanNotFoundException(kodeKegiatan);
        }
        
        return kegiatanRepository.save(kegiatan);
    }

    public void hapusKegiatan(String kodeKegiatan) {
        Kegiatan kegiatan = kegiatanRepository.findByKodeKegiatan(kodeKegiatan)
                .orElseThrow(() -> new KegiatanNotFoundException(kodeKegiatan));

        if (subKegiatanRepository.existsByKegiatanId(kegiatan.id())) {
            throw new KegiatanDeleteForbiddenException(kodeKegiatan);
        }

        kegiatanRepository.deleteByKodeKegiatan(kodeKegiatan);
    }

    public List<Kegiatan> detailKegiatanByKodeKegiatanIn(List<String> kodeKegiatans) {
        List<Kegiatan> kegiatans = kegiatanRepository.findAllByKodeKegiatanIn(kodeKegiatans);

        if (kegiatans.size() != kodeKegiatans.size()) {
            Set<String> foundKode = kegiatans.stream()
                    .map(Kegiatan::kodeKegiatan)
                    .collect(Collectors.toSet());

            String missingKode = kodeKegiatans.stream()
                    .filter(kode -> !foundKode.contains(kode))
                    .findFirst()
                    .orElse(null);

            if (missingKode != null) {
                throw new KegiatanNotFoundException(missingKode);
            }
        }

        return kegiatans;
    }

    public String getKodeProgram(Long programId) {
        return programRepository.findById(programId)
                .map(Program::kodeProgram)
                .orElseThrow(() -> new ProgramNotFoundException(String.valueOf(programId)));
    }
}
