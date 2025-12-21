package cc.kertaskerja.bontang.rencanaaksi.domain;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.rencanaaksi.domain.exception.RencanaAksiNotFoundException;

@Service
public class RencanaAksiService {
    private RencanaAksiRepository rencanaAksiRepository;

    public RencanaAksiService(RencanaAksiRepository rencanaAksiRepository) {
        this.rencanaAksiRepository = rencanaAksiRepository;
    }

    public Iterable<RencanaAksi> findAll() {
        return rencanaAksiRepository.findAll();
    }

    public RencanaAksi detailRencanaAksiById(Long id) {
        return rencanaAksiRepository.findById(id)
                .orElseThrow(() -> new RencanaAksiNotFoundException(id));
    }

    public RencanaAksi tambahRencanaAksi(RencanaAksi rencanaAksi) {

        return rencanaAksiRepository.save(rencanaAksi);
    }

    public RencanaAksi ubahRencanaAksi(Long id, RencanaAksi rencanaAksi) {
        if (!rencanaAksiRepository.existsById(id)) {
            throw new RencanaAksiNotFoundException(id);
        }

        return rencanaAksiRepository.save(rencanaAksi);
    }

    public void hapusRencanaAksi(Long id) {
        if (!rencanaAksiRepository.existsById(id)) {
            throw new RencanaAksiNotFoundException(id);
        }

        rencanaAksiRepository.deleteById(id);
    }

    public Iterable<RencanaAksi> findByIdRekinOrderByUrutan(Integer idRencanaKinerja) {
        return rencanaAksiRepository.findByIdRekinOrderByUrutan(idRencanaKinerja);
    }
}
