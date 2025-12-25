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

    public Iterable<DasarHukum> findByKodeOpd(String kodeOpd) {
        return dasarHukumRepository.findByKodeOpd(kodeOpd);
    }

    public Iterable<DasarHukum> findByIdRencanaKinerja(Long idRencanaKinerja) {
        return dasarHukumRepository.findByIdRencanaKinerja(idRencanaKinerja);
    }

    public DasarHukum detailDasarHukumById(Long id) {
        return dasarHukumRepository.findById(id)
                .orElseThrow(() -> new DasarHukumNotFoundException(id));
    }

    public DasarHukum detailDasarHukumByIdRencanaKinerja(Long idRencanaKinerja, Long id) {
        return dasarHukumRepository.findByIdRencanaKinerjaAndId(idRencanaKinerja, id)
                .orElseThrow(() -> new DasarHukumNotFoundException(id));
    }

    public DasarHukum tambahDasarHukum(DasarHukum dasarHukum) {

        return dasarHukumRepository.save(dasarHukum);
    }

    public DasarHukum ubahDasarHukum(Long idRencanaKinerja, Long id, DasarHukum dasarHukum) {
        if (!dasarHukumRepository.findByIdRencanaKinerjaAndId(idRencanaKinerja, id).isPresent()) {
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