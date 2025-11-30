package cc.kertaskerja.bontang.subkegiatan.domain;

import cc.kertaskerja.bontang.kegiatan.domain.Kegiatan;
import cc.kertaskerja.bontang.kegiatan.domain.KegiatanRepository;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanNotFoundException;
import cc.kertaskerja.bontang.subkegiatan.domain.exception.SubKegiatanNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubKegiatanService {
    private final SubKegiatanRepository subKegiatanRepository;
    private final KegiatanRepository kegiatanRepository;

    public SubKegiatanService(
            SubKegiatanRepository subKegiatanRepository,
            KegiatanRepository kegiatanRepository
    ) {
        this.subKegiatanRepository = subKegiatanRepository;
        this.kegiatanRepository = kegiatanRepository;
    }

    public Iterable<SubKegiatan> findAll() {
        return subKegiatanRepository.findAll();
    }

    public SubKegiatan detailSubKegiatanByKodeSubKegiatan(String kodeSubKegiatan) {
        return subKegiatanRepository.findByKodeSubKegiatan(kodeSubKegiatan)
                .orElseThrow(() -> new SubKegiatanNotFoundException(kodeSubKegiatan));
    }

    public SubKegiatan tambahSubKegiatan(SubKegiatan subKegiatan, String kodeKegiatan) {
        Long kegiatanId = getKegiatanId(kodeKegiatan);
        SubKegiatan subKegiatanWithParent = attachKegiatan(subKegiatan, kegiatanId);

        return subKegiatanRepository.save(subKegiatanWithParent);
    }

    public SubKegiatan ubahSubKegiatan(String kodeSubKegiatan, SubKegiatan subKegiatan, String kodeKegiatan) {
        if (!subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)) {
            throw new SubKegiatanNotFoundException(kodeSubKegiatan);
        }

        Long kegiatanId = getKegiatanId(kodeKegiatan);
        SubKegiatan subKegiatanWithParent = attachKegiatan(subKegiatan, kegiatanId);
        return subKegiatanRepository.save(subKegiatanWithParent);
    }

    public void hapusSubKegiatan(String kodeSubKegiatan) {
        if (!subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)) {
            throw new SubKegiatanNotFoundException(kodeSubKegiatan);
        }

        subKegiatanRepository.deleteByKodeSubKegiatan(kodeSubKegiatan);
    }

    public List<SubKegiatan> detailSubKegiatanByKodeSubKegiatanIn(List<String> kodeSubKegiatans) {
        List<SubKegiatan> subKegiatans = subKegiatanRepository.findAllByKodeSubKegiatanIn(kodeSubKegiatans);
        if (subKegiatans.size() != kodeSubKegiatans.size()) {
            Set<String> foundKode = subKegiatans.stream()
                    .map(SubKegiatan::kodeSubKegiatan)
                    .collect(Collectors.toSet());

            String missingKode = kodeSubKegiatans.stream()
                    .filter(kode -> !foundKode.contains(kode))
                    .findFirst()
                    .orElse(null);

            if (missingKode != null) {
                throw new SubKegiatanNotFoundException(missingKode);
            }
        }

        return subKegiatans;
    }

    private Long getKegiatanId(String kodeKegiatan) {
        return kegiatanRepository.findByKodeKegiatan(kodeKegiatan)
                .map(Kegiatan::id)
                .orElseThrow(() -> new KegiatanNotFoundException(kodeKegiatan));
    }

    private SubKegiatan attachKegiatan(SubKegiatan subKegiatan, Long kegiatanId) {
        return new SubKegiatan(
                subKegiatan.id(),
                subKegiatan.kodeSubKegiatan(),
                subKegiatan.namaSubKegiatan(),
                kegiatanId,
                subKegiatan.createdDate(),
                subKegiatan.lastModifiedDate()
        );
    }
}
