package cc.kertaskerja.bontang.subkegiatan.domain;

import cc.kertaskerja.bontang.shared.OpdPrefixExtractor;
import cc.kertaskerja.bontang.subkegiatan.domain.exception.SubKegiatanNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SubKegiatanService {
    private final SubKegiatanRepository subKegiatanRepository;

    public SubKegiatanService(
            SubKegiatanRepository subKegiatanRepository
    ) {
        this.subKegiatanRepository = subKegiatanRepository;
    }

    public Iterable<SubKegiatan> findAll() {
        return subKegiatanRepository.findAll();
    }

    public List<SubKegiatan> detailSubKegiatanIn(List<String> kodeSubKegiatans) {
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

    public SubKegiatan detailSubKegiatanByKodeSubKegiatan(String kodeSubKegiatan) {
        return subKegiatanRepository.findByKodeSubKegiatan(kodeSubKegiatan)
                .orElseThrow(() -> new SubKegiatanNotFoundException(kodeSubKegiatan));
    }

    public List<SubKegiatan> findSubKegiatansForKodeOpd(String kodeOpd) {
        List<SubKegiatan> subKegiatans = toList(subKegiatanRepository.findByKodeOpd(kodeOpd));
        if (!subKegiatans.isEmpty()) {
            return subKegiatans;
        }

        String prefix = OpdPrefixExtractor.extractPrefix(kodeOpd, 2);
        if (prefix == null) {
            return List.of();
        }

        return subKegiatanRepository.findByKodeSubKegiatanStartingWith(prefix);
    }

    public SubKegiatan tambahSubKegiatan(SubKegiatan subKegiatan) {

        return subKegiatanRepository.save(subKegiatan);
    }

    public SubKegiatan ubahSubKegiatan(String kodeSubKegiatan, SubKegiatan subKegiatan) {
        if (!subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)) {
            throw new SubKegiatanNotFoundException(kodeSubKegiatan);
        }

        return subKegiatanRepository.save(subKegiatan);
    }

    public void hapusSubKegiatan(String kodeSubKegiatan) {
        if (!subKegiatanRepository.existsByKodeSubKegiatan(kodeSubKegiatan)) {
            throw new SubKegiatanNotFoundException(kodeSubKegiatan);
        }

        subKegiatanRepository.deleteByKodeSubKegiatan(kodeSubKegiatan);
    }

    private List<SubKegiatan> toList(Iterable<SubKegiatan> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }
}
