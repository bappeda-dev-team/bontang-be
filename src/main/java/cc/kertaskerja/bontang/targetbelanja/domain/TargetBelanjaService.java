package cc.kertaskerja.bontang.targetbelanja.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.targetbelanja.domain.exception.TargetBelanjaNotFoundException;
import cc.kertaskerja.bontang.targetbelanja.web.TargetBelanjaRequest;

@Service
public class TargetBelanjaService {
    private TargetBelanjaRepository targetBelanjaRepository;

    public TargetBelanjaService(TargetBelanjaRepository targetBelanjaRepository) {
        this.targetBelanjaRepository = targetBelanjaRepository;
    }

    public Iterable<TargetBelanja> findAll() {
        return targetBelanjaRepository.findAll();
    }

    public TargetBelanja detailTargetBelanjaById(Long id) {
        return targetBelanjaRepository.findById(id)
                .orElseThrow(() -> new TargetBelanjaNotFoundException(id));
    }

    public List<TargetBelanja> findByIndikatorBelanjaId(Long indikatorBelanjaId) {
        return targetBelanjaRepository.findByIndikatorBelanjaId(indikatorBelanjaId);
    }

    public TargetBelanja tambahTargetBelanja(TargetBelanjaRequest request) {
        TargetBelanja target = TargetBelanja.of(
                request.namaTargetBelanja(),
                request.satuanTargetBelanja(),
                request.indikatorBelanjaId()
        );
        return targetBelanjaRepository.save(target);
    }

    public TargetBelanja ubahTargetBelanja(Long id, TargetBelanjaRequest request) {
        TargetBelanja existingTargetBelanja = detailTargetBelanjaById(id);

        TargetBelanja targetBelanja = new TargetBelanja(
                existingTargetBelanja.id(),
                request.namaTargetBelanja(),
                request.satuanTargetBelanja(),
                request.indikatorBelanjaId(),
                existingTargetBelanja.createdDate(),
                existingTargetBelanja.lastModifiedDate()
        );

        return targetBelanjaRepository.save(targetBelanja);
    }

    public void hapusTargetBelanja(Long id) {
        if (!targetBelanjaRepository.existsById(id)) {
            throw new TargetBelanjaNotFoundException(id);
        }

        targetBelanjaRepository.deleteById(id);
    }
}
