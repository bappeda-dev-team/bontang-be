package cc.kertaskerja.bontang.kegiatanopd.domain;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.kegiatan.domain.Kegiatan;
import cc.kertaskerja.bontang.kegiatanopd.domain.exception.KegiatanOpdNotFoundException;

@Service
public class KegiatanOpdService {
	private final KegiatanOpdRepository kegiatanOpdRepository;

    public KegiatanOpdService(KegiatanOpdRepository kegiatanOpdRepository) {
        this.kegiatanOpdRepository = kegiatanOpdRepository;
    }

    public Iterable<KegiatanOpd> findAll() {
        return kegiatanOpdRepository.findAll();
    }

    public Iterable<KegiatanOpd> findByKodeOpdAndTahun(String kodeOpd, Integer tahun) {
        return kegiatanOpdRepository.findByKodeOpdAndTahun(kodeOpd, tahun);
    }

    public KegiatanOpd detailKegiatanOpdByKodeKegiatan(String kodeKegiatanOpd) {
        return kegiatanOpdRepository.findByKodeKegiatanOpd(kodeKegiatanOpd)
                .orElseThrow(() -> new KegiatanOpdNotFoundException(kodeKegiatanOpd));
    }

    public KegiatanOpd tambahKegiatanOpd(KegiatanOpd kegiatanOpd) {

        return kegiatanOpdRepository.save(kegiatanOpd);
    }

    public KegiatanOpd simpanKegiatanOpd(Kegiatan kegiatan) {
        KegiatanOpd kegiatanOpd = kegiatanOpdRepository.findByKodeKegiatanOpd(kegiatan.kodeKegiatan())
                .map(existing -> new KegiatanOpd(
                        existing.id(),
                        kegiatan.kodeKegiatan(),
                        kegiatan.namaKegiatan(),
                        kegiatan.kodeOpd(),
                        kegiatan.tahun(),
                        existing.createdDate(),
                        null
                ))
                .orElseGet(() -> KegiatanOpd.of(
                        kegiatan.kodeKegiatan(),
                        kegiatan.namaKegiatan(),
                        kegiatan.kodeOpd(),
                        kegiatan.tahun()
                ));

        return kegiatanOpdRepository.save(kegiatanOpd);
    }

    public KegiatanOpd ubahKegiatanOpd(String kodeKegiatanOpd, KegiatanOpd kegiatanOpd) {
        if (!kegiatanOpdRepository.existsByKodeKegiatanOpd(kodeKegiatanOpd)) {
            throw new KegiatanOpdNotFoundException(kodeKegiatanOpd);
        }

        return kegiatanOpdRepository.save(kegiatanOpd);
    }

    public void hapusKegiatanOpd(String kodeKegiatanOpd) {
        if (!kegiatanOpdRepository.existsByKodeKegiatanOpd(kodeKegiatanOpd)) {
            throw new KegiatanOpdNotFoundException(kodeKegiatanOpd);
        }

        kegiatanOpdRepository.deleteByKodeKegiatanOpd(kodeKegiatanOpd);
    }
}
