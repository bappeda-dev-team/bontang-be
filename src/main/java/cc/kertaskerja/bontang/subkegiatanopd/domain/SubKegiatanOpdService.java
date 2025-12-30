package cc.kertaskerja.bontang.subkegiatanopd.domain;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.kegiatanopd.domain.exception.KegiatanOpdNotFoundException;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatan;
import cc.kertaskerja.bontang.subkegiatanopd.domain.exception.SubKegiatanOpdNotFoundException;

@Service
public class SubKegiatanOpdService {
    private final SubKegiatanOpdRepository subKegiatanOpdRepository;

    public SubKegiatanOpdService(SubKegiatanOpdRepository subKegiatanOpdRepository) {
        this.subKegiatanOpdRepository = subKegiatanOpdRepository;
    }

    public Iterable<SubKegiatanOpd> findAll() {
        return subKegiatanOpdRepository.findAll();
    }

    public Iterable<SubKegiatanOpd> findByKodeOpdAndTahun(String kodeOpd, Integer tahun) {
        return subKegiatanOpdRepository.findByKodeOpdAndTahun(kodeOpd, tahun);
    }

    public SubKegiatanOpd detailSubKegiatanOpdByKodeSubKegiatan(String kodeSubKegiatanOpd) {
        return subKegiatanOpdRepository.findByKodeSubKegiatanOpd(kodeSubKegiatanOpd)
                .orElseThrow(() -> new SubKegiatanOpdNotFoundException(kodeSubKegiatanOpd));
    }

    public SubKegiatanOpd tambahSubKegiatanOpd(SubKegiatanOpd subKegiatanOpd) {

        return subKegiatanOpdRepository.save(subKegiatanOpd);
    }

    public SubKegiatanOpd simpanSubKegiatanOpd(SubKegiatan subKegiatan) {
        SubKegiatanOpd subKegiatanOpd = subKegiatanOpdRepository.findByKodeSubKegiatanOpd(subKegiatan.kodeSubKegiatan())
                .map(existing -> new SubKegiatanOpd(
                        existing.id(),
                        subKegiatan.kodeSubKegiatan(),
                        subKegiatan.namaSubKegiatan(),
                        subKegiatan.kodeOpd(),
                        subKegiatan.tahun(),
                        existing.createdDate(),
                        null
                ))
                .orElseGet(() -> SubKegiatanOpd.of(
                        subKegiatan.kodeSubKegiatan(),
                        subKegiatan.namaSubKegiatan(),
                        subKegiatan.kodeOpd(),
                        subKegiatan.tahun()
                ));

        return subKegiatanOpdRepository.save(subKegiatanOpd);
    }

    public SubKegiatanOpd ubahSubKegiatanOpd(String kodeSubKegiatanOpd, SubKegiatanOpd subKegiatanOpd) {
        if (!subKegiatanOpdRepository.existsByKodeSubKegiatanOpd(kodeSubKegiatanOpd)) {
            throw new KegiatanOpdNotFoundException(kodeSubKegiatanOpd);
        }

        return subKegiatanOpdRepository.save(subKegiatanOpd);
    }

    public void hapusSubKegiatanOpd(String kodeSubKegiatanOpd) {
        if (!subKegiatanOpdRepository.existsByKodeSubKegiatanOpd(kodeSubKegiatanOpd)) {
            throw new KegiatanOpdNotFoundException(kodeSubKegiatanOpd);
        }

        subKegiatanOpdRepository.deleteByKodeSubKegiatanOpd(kodeSubKegiatanOpd);
    }
}
