package cc.kertaskerja.bontang.rinciananggaran.domain;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.rinciananggaran.domain.exception.RincianAnggaranNotFoundException;
import cc.kertaskerja.bontang.rinciananggaran.web.RincianAnggaranRequest;

@Service
public class RincianAnggaranService {
    private RincianAnggaranRepository rincianAnggaranRepository;

    public RincianAnggaranService(RincianAnggaranRepository rincianAnggaranRepository) {
        this.rincianAnggaranRepository = rincianAnggaranRepository;
    }

    public RincianAnggaran detailRincianAnggaranById(Long id) {
        return rincianAnggaranRepository.findById(id)
                .orElseThrow(() -> new RincianAnggaranNotFoundException(id));
    }

    public Iterable<RincianAnggaran> findAll() {
        return rincianAnggaranRepository.findAll();
    }

    public Iterable<RincianAnggaran> findByIdRincianAnggaranOrderByUrutan(Integer idRincianAnggaran) {
        return rincianAnggaranRepository.findByIdRincianAnggaranOrderByUrutan(idRincianAnggaran);
    }

    public RincianAnggaran tambahRincianAnggaran(Integer idRincianBelanja, RincianAnggaranRequest request) {
        RincianAnggaran rincianAnggaran = RincianAnggaran.of(
                idRincianBelanja,
                request.namaRincianAnggaran(),
                request.urutan()
        );
        return rincianAnggaranRepository.save(rincianAnggaran);
    }

    public RincianAnggaran ubahRincianAnggaran(Long id, Integer idRincianBelanja, RincianAnggaranRequest request) {
        RincianAnggaran existingRincianAnggaran = detailRincianAnggaranById(id);

        if (!existingRincianAnggaran.idRincianBelanja().equals(idRincianBelanja)) {
            throw new RincianAnggaranNotFoundException(id);
        }

        RincianAnggaran rincianAnggaran = new RincianAnggaran(
                existingRincianAnggaran.id(),
                existingRincianAnggaran.idRincianBelanja(),
                request.namaRincianAnggaran(),
                request.urutan(),
                existingRincianAnggaran.createdDate(),
                null
        );

        return rincianAnggaranRepository.save(rincianAnggaran);
    }

    public void hapusRincianAnggaran(Long id) {
        if (!rincianAnggaranRepository.existsById(id)) {
            throw new RincianAnggaranNotFoundException(id);
        }

        rincianAnggaranRepository.deleteById(id);
    }
}
