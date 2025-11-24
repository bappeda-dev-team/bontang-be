package cc.kertaskerja.bontang.subkegiatan.domain;

import cc.kertaskerja.bontang.subkegiatan.domain.exception.SubKegiatanNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SubKegiatanService {
    private SubKegiatanRepository subKegiatanRepository;

    public SubKegiatanService(SubKegiatanRepository subKegiatanRepository) {
        this.subKegiatanRepository = subKegiatanRepository;
    }

    public Iterable<SubKegiatan> findAll() {
        return subKegiatanRepository.findAll();
    }

    public SubKegiatan detailSubKegiatanByKodeSubKegiatan(String kodeSubKegiatan) {
        return subKegiatanRepository.findByKodeSubKegiatan(kodeSubKegiatan)
                .orElseThrow(() -> new SubKegiatanNotFoundException(kodeSubKegiatan));
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
}
