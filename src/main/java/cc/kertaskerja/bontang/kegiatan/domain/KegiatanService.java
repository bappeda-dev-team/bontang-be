package cc.kertaskerja.bontang.kegiatan.domain;

import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KegiatanService{
    private final KegiatanRepository kegiatanRepository;

    public KegiatanService(
            KegiatanRepository kegiatanRepository
    ) {
        this.kegiatanRepository = kegiatanRepository;
    }

    public Iterable<Kegiatan> findAll() {
        return kegiatanRepository.findAll();
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
        if (!kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)) {
            throw new KegiatanNotFoundException(kodeKegiatan);
        }

        kegiatanRepository.deleteByKodeKegiatan(kodeKegiatan);
    }
}
