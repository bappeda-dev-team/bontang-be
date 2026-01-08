package cc.kertaskerja.bontang.indikatorbelanja.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.indikatorbelanja.domain.exception.IndikatorBelanjaNotFoundException;
import cc.kertaskerja.bontang.indikatorbelanja.web.IndikatorBelanjaRequest;

@Service
public class IndikatorBelanjaService {
    private IndikatorBelanjaRepository indikatorBelanjaRepository;

    public IndikatorBelanjaService(IndikatorBelanjaRepository indikatorBelanjaRepository) {
        this.indikatorBelanjaRepository = indikatorBelanjaRepository;
    }

    public Iterable<IndikatorBelanja> findAll() {
        return indikatorBelanjaRepository.findAll();
    }

    public IndikatorBelanja detailIndikatorBelanjaById(Long id) {
        return indikatorBelanjaRepository.findById(id)
                .orElseThrow(() -> new IndikatorBelanjaNotFoundException(id));
    }

    public List<IndikatorBelanja> findByRincianBelanjaId(Long rincianBelanjaId) {
        return indikatorBelanjaRepository.findByRincianBelanjaId(rincianBelanjaId);
    }

    public IndikatorBelanja tambahIndikatorBelanja(IndikatorBelanjaRequest request) {
        IndikatorBelanja indikator = IndikatorBelanja.of(
                request.namaIndikatorBelanja(),
                request.rincianBelanjaId()
        );
        return indikatorBelanjaRepository.save(indikator);
    }

    public IndikatorBelanja ubahIndikatorBelanja(Long id, IndikatorBelanjaRequest request) {
        IndikatorBelanja existingIndikatorBelanja = detailIndikatorBelanjaById(id);

        IndikatorBelanja indikatorBelanja = new IndikatorBelanja(
                existingIndikatorBelanja.id(),
                request.namaIndikatorBelanja(),
                request.rincianBelanjaId(),
                existingIndikatorBelanja.createdDate(),
                existingIndikatorBelanja.lastModifiedDate()
        );

        return indikatorBelanjaRepository.save(indikatorBelanja);
    }

    public void hapusIndikatorBelanja(Long id) {
        if (!indikatorBelanjaRepository.existsById(id)) {
            throw new IndikatorBelanjaNotFoundException(id);
        }

        indikatorBelanjaRepository.deleteById(id);
    }
}
