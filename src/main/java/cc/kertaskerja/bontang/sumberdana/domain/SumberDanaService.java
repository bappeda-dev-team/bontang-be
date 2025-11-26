package cc.kertaskerja.bontang.sumberdana.domain;

import cc.kertaskerja.bontang.sumberdana.domain.exception.SumberDanaNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SumberDanaService {
    private SumberDanaRepository sumberDanaRepository;

    public SumberDanaService(SumberDanaRepository sumberDanaRepository) {
        this.sumberDanaRepository = sumberDanaRepository;
    }

    public Iterable<SumberDana> findAll() {
        return sumberDanaRepository.findAll();
    }

    public SumberDana detailSumberDanaById(Long id) {
        return sumberDanaRepository.findById(id)
                .orElseThrow(() -> new SumberDanaNotFoundException(id));
    }

    public SumberDana tambahSumberDana(SumberDana sumberDana) {

        return sumberDanaRepository.save(sumberDana);
    }

    public SumberDana ubahSumberDana(Long id, SumberDana sumberDana) {
        if (!sumberDanaRepository.existsById(id)) {
            throw new SumberDanaNotFoundException(id);
        }

        return sumberDanaRepository.save(sumberDana);
    }

    public void hapusSumberDana(Long id) {
        if (!sumberDanaRepository.existsById(id)) {
            throw new SumberDanaNotFoundException(id);
        }

        sumberDanaRepository.deleteById(id);
    }
}
