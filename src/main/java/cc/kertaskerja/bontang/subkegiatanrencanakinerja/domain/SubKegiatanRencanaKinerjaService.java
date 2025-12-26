package cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain;

import cc.kertaskerja.bontang.subkegiatanrencanakinerja.web.SubKegiatanRencanaKinerjaRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubKegiatanRencanaKinerjaService {
	private SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository;

    public SubKegiatanRencanaKinerjaService(SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository) {
        this.subKegiatanRencanaKinerjaRepository = subKegiatanRencanaKinerjaRepository;
    }

    public Iterable<SubKegiatanRencanaKinerja> findAll() {
        return subKegiatanRencanaKinerjaRepository.findAll();
    }

    public SubKegiatanRencanaKinerja detailSubKegiatanRencanaKinerjaById(Long id) {
        return subKegiatanRencanaKinerjaRepository.findById(id)
                .orElseThrow(() -> new SubKegiatanRencanaKinerjaNotFoundException(id));
    }

    public List<SubKegiatanRencanaKinerja> findByIdRekin(Integer idRekin) {
        return subKegiatanRencanaKinerjaRepository.findByIdRekin(idRekin);
    }

    public SubKegiatanRencanaKinerja tambahSubKegiatanRencanaKinerja(String kodeSubKegiatan, String namaSubKegiatan, Integer idRekin) {
        SubKegiatanRencanaKinerja subKegiatanRencanaKinerja = SubKegiatanRencanaKinerja.of(idRekin, kodeSubKegiatan, namaSubKegiatan);
        return subKegiatanRencanaKinerjaRepository.save(subKegiatanRencanaKinerja);
    }

    public SubKegiatanRencanaKinerja tambahSubKegiatanRencanaKinerja(SubKegiatanRencanaKinerja subKegiatanRencanaKinerja) {
        return subKegiatanRencanaKinerjaRepository.save(subKegiatanRencanaKinerja);
    }

    public SubKegiatanRencanaKinerja tambahSubKegiatanRencanaKinerja(SubKegiatanRencanaKinerjaRequest request, Integer idRekin) {
        SubKegiatanRencanaKinerja subKegiatanRencanaKinerja = SubKegiatanRencanaKinerja.of(
                idRekin,
                request.kodeSubKegiatanRencanaKinerja(),
                request.namaSubKegiatanRencanaKinerja()
        );
        return subKegiatanRencanaKinerjaRepository.save(subKegiatanRencanaKinerja);
    }

    public SubKegiatanRencanaKinerja ubahSubKegiatanRencanaKinerja(Long id, SubKegiatanRencanaKinerja subKegiatanRencanaKinerja) {
        if (!subKegiatanRencanaKinerjaRepository.existsById(id)) {
            throw new SubKegiatanRencanaKinerjaNotFoundException(id);
        }

        return subKegiatanRencanaKinerjaRepository.save(subKegiatanRencanaKinerja);
    }

    public void hapusSubKegiatanRencanaKinerja(Long id) {
        if (!subKegiatanRencanaKinerjaRepository.existsById(id)) {
            throw new SubKegiatanRencanaKinerjaNotFoundException(id);
        }

        subKegiatanRencanaKinerjaRepository.deleteSubKegiatanRencanaKinerjaById(id);
    }
}
