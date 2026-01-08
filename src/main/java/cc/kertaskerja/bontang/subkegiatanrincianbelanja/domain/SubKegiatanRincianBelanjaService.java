package cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain.exception.SubKegiatanRincianBelanjaNotFoundException;
import cc.kertaskerja.bontang.subkegiatanrincianbelanja.web.SubKegiatanRincianBelanjaRequest;


@Service
public class SubKegiatanRincianBelanjaService {
    private SubKegiatanRincianBelanjaRepository subKegiatanRincianBelanjaRepository;

    public SubKegiatanRincianBelanjaService(SubKegiatanRincianBelanjaRepository subKegiatanRincianBelanjaRepository) {
        this.subKegiatanRincianBelanjaRepository = subKegiatanRincianBelanjaRepository;
    }

    public Iterable<SubKegiatanRincianBelanja> findAll() {
        return subKegiatanRincianBelanjaRepository.findAll();
    }

    public List<SubKegiatanRincianBelanja> findByIdRincianBelanja(Integer idRincianBelanja) {
        return subKegiatanRincianBelanjaRepository.findByIdRincianBelanja(idRincianBelanja);
    }

    public SubKegiatanRincianBelanja detailSubKegiatanRincianBelanjaByKodeSubKegiatan(String kodeSubKegiatan) {
        return subKegiatanRincianBelanjaRepository.findByKodeSubKegiatanRincianBelanja(kodeSubKegiatan)
                .orElseThrow(() -> new SubKegiatanRincianBelanjaNotFoundException(kodeSubKegiatan));
    }

    public SubKegiatanRincianBelanja findByIdRincianBelanjaAndKodeSubKegiatan(Integer idRincianBelanja, String kodeSubKegiatan) {
        return subKegiatanRincianBelanjaRepository.findByIdRincianBelanjaAndKodeSubKegiatanRincianBelanja(idRincianBelanja, kodeSubKegiatan)
                .orElseThrow(() -> new SubKegiatanRincianBelanjaNotFoundException(kodeSubKegiatan));
    }

    public SubKegiatanRincianBelanja tambahSubKegiatanRincianBelanja(SubKegiatanRincianBelanjaRequest request, Integer idRincianBelanja) {
        SubKegiatanRincianBelanja subKegiatanRincianBelanja = SubKegiatanRincianBelanja.of(
                idRincianBelanja,
                request.kodeSubKegiatanRincianKinerja(),
                request.namaSubKegiatanRincianKinerja(),
                request.pagu()
        );
        return subKegiatanRincianBelanjaRepository.save(subKegiatanRincianBelanja);
    }

    public SubKegiatanRincianBelanja ubahSubKegiatanRincianBelanja(String kodeSubKegiatan, SubKegiatanRincianBelanjaRequest request) {
        SubKegiatanRincianBelanja existingSubKegiatan = detailSubKegiatanRincianBelanjaByKodeSubKegiatan(kodeSubKegiatan);
        
        SubKegiatanRincianBelanja updatedSubKegiatan = new SubKegiatanRincianBelanja(
                existingSubKegiatan.id(),
                existingSubKegiatan.idRincianBelanja(),
                request.kodeSubKegiatanRincianKinerja(),
                request.namaSubKegiatanRincianKinerja(),
                request.pagu(),
                existingSubKegiatan.createdDate(),
                Instant.now()
        );

        return subKegiatanRincianBelanjaRepository.save(updatedSubKegiatan);
    }

    public void hapusSubKegiatanRincianBelanja(String kodeSubKegiatan) {
        if (subKegiatanRincianBelanjaRepository.findByKodeSubKegiatanRincianBelanja(kodeSubKegiatan).isEmpty()) {
            throw new SubKegiatanRincianBelanjaNotFoundException(kodeSubKegiatan);
        }

        subKegiatanRincianBelanjaRepository.deleteByKodeSubKegiatanRincianBelanja(kodeSubKegiatan);
    }
}
