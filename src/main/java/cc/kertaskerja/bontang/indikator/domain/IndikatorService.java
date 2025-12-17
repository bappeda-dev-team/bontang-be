package cc.kertaskerja.bontang.indikator.domain;

import org.springframework.stereotype.Service;

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

    public Indikator tambahIndikator(Indikator indikator) {

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