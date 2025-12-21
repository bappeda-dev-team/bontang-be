package cc.kertaskerja.bontang.indikator.domain;

import cc.kertaskerja.bontang.indikator.web.IndikatorRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndikatorService {
	private IndikatorRepository indikatorRepository;

    public IndikatorService(IndikatorRepository indikatorRepository) {
        this.indikatorRepository = indikatorRepository;
    }

    public Iterable<Indikator> findAll() {
        return indikatorRepository.findAll();
    }

    public Indikator detailIndikatorById(Long id) {
        return indikatorRepository.findById(id)
                .orElseThrow(() -> new IndikatorNotFoundException(id));
    }

    public List<Indikator> findByRencanaKinerjaId(Long rencanaKinerjaId) {
        return indikatorRepository.findByRencanaKinerjaId(rencanaKinerjaId);
    }

    public Indikator tambahIndikator(String namaIndikator, Long rencanaKinerjaId) {
        Indikator indikator = Indikator.of(namaIndikator, rencanaKinerjaId);
        return indikatorRepository.save(indikator);
    }

    public Indikator tambahIndikator(Indikator indikator) {
        return indikatorRepository.save(indikator);
    }

    public Indikator tambahIndikator(IndikatorRequest request) {
        Indikator indikator = Indikator.of(
                request.namaIndikator(),
                request.rencanaKinerjaId()
        );
        return indikatorRepository.save(indikator);
    }

    public Indikator ubahIndikator(Long id, Indikator indikator) {
        if (!indikatorRepository.existsById(id)) {
            throw new IndikatorNotFoundException(id);
        }

        return indikatorRepository.save(indikator);
    }

    public void hapusIndikator(Long id) {
        if (!indikatorRepository.existsById(id)) {
            throw new IndikatorNotFoundException(id);
        }

        indikatorRepository.deleteById(id);
    }
}
