package cc.kertaskerja.bontang.dasarhukum.domain;

import org.springframework.stereotype.Service;

@Service
public class DasarHukumService {
	private DasarHukumRepository dasarHukumRepository;

    public DasarHukumService(DasarHukumRepository dasarHukumRepository) {
        this.dasarHukumRepository = dasarHukumRepository;
    }

    public Iterable<DasarHukum> findAll() {
        return dasarHukumRepository.findAll();
    }

    public DasarHukum detailDasarHukumById(Long id) {
        return dasarHukumRepository.findById(id)
                .orElseThrow(() -> new DasarHukumNotFoundException(id));
    }

    public DasarHukum tambahDasarHukum(DasarHukum dasarHukum) {

        return dasarHukumRepository.save(dasarHukum);
    }

    public DasarHukum ubahDasarHukum(Long id, DasarHukum dasarHukum) {
        if (!dasarHukumRepository.existsById(id)) {
            throw new DasarHukumNotFoundException(id);
        }

        return dasarHukumRepository.save(dasarHukum);
    }

    public void hapusDasarHukum(Long id) {
        if (!dasarHukumRepository.existsById(id)) {
            throw new DasarHukumNotFoundException(id);
        }

        dasarHukumRepository.deleteById(id);
    }
}