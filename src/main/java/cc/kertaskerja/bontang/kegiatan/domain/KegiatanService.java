package cc.kertaskerja.bontang.kegiatan.domain;

import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanAlreadyExistException;
import cc.kertaskerja.bontang.kegiatan.domain.exception.KegiatanNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class KegiatanService{
    public KegiatanRepository kegiatanRepository;

    public KegiatanService(KegiatanRepository kegiatanRepository) {
        this.kegiatanRepository = kegiatanRepository;
    }

    public Iterable<Kegiatan> findAll() {
        return kegiatanRepository.findAll();
    }

    public Kegiatan detailKegiatanByKodeKegiatan(String kodeKegiatan) {
        return kegiatanRepository.findByKodeKegiatan(kodeKegiatan)
                .orElseThrow(() -> new KegiatanNotFoundException(kodeKegiatan));
    }

    public Kegiatan tambahKegiatan(Kegiatan kegiatan) {
        String kodeKegiatan = kegiatan.kodeKegiatan();
        if (kegiatanRepository.existsByKodeKegiatan(kodeKegiatan)) {
            throw new KegiatanAlreadyExistException(kodeKegiatan);
        }

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
