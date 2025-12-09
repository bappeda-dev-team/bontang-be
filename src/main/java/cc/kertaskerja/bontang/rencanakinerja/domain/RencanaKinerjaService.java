package cc.kertaskerja.bontang.rencanakinerja.domain;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaNotFoundException;

@Service
public class RencanaKinerjaService {
    private RencanaKinerjaRepository rencanaKinerjaRepository;

    public RencanaKinerjaService(RencanaKinerjaRepository rencanaKinerjaRepository) {
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
    }

    public Iterable<RencanaKinerja> findAll() {
        return rencanaKinerjaRepository.findAll();
    }

    public RencanaKinerja detailRencanaKinerjaById(Long id) {
        return rencanaKinerjaRepository.findById(id)
                .orElseThrow(() -> new RencanaKinerjaNotFoundException(id));
    }

    public RencanaKinerja tambahRencanaKinerja(RencanaKinerja rencanaKinerja) {

        return rencanaKinerjaRepository.save(rencanaKinerja);
    }

    public RencanaKinerja ubahRencanaKinerja(Long id, RencanaKinerja rencanaKinerja) {
        if (!rencanaKinerjaRepository.existsById(id)) {
            throw new RencanaKinerjaNotFoundException(id);
        }

        return rencanaKinerjaRepository.save(rencanaKinerja);
    }

    public void hapusRencanaKinerja(Long id) {
        if (!rencanaKinerjaRepository.existsById(id)) {
            throw new RencanaKinerjaNotFoundException(id);
        }

        rencanaKinerjaRepository.deleteById(id);
    }
}
