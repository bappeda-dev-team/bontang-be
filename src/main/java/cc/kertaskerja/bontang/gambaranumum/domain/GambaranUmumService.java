package cc.kertaskerja.bontang.gambaranumum.domain;

import org.springframework.stereotype.Service;

@Service
public class GambaranUmumService {
	private GambaranUmumRepository gambaranUmumRepository;

    public GambaranUmumService(GambaranUmumRepository gambaranUmumRepository) {
        this.gambaranUmumRepository = gambaranUmumRepository;
    }

    public Iterable<GambaranUmum> findAll() {
        return gambaranUmumRepository.findAll();
    }

    public GambaranUmum detailGambaranUmumById(Long id) {
        return gambaranUmumRepository.findById(id)
                .orElseThrow(() -> new GambaranUmumNotFoundException(id));
    }

    public Iterable<GambaranUmum> findByKodeOpd(String kodeOpd) {
        return gambaranUmumRepository.findByKodeOpd(kodeOpd);
    }

    public Iterable<GambaranUmum> findByIdRencanaKinerja(Long idRencanaKinerja) {
        return gambaranUmumRepository.findByIdRencanaKinerja(idRencanaKinerja);
    }

    public GambaranUmum detailGambaranUmumByIdRencanaKinerja(Long idRencanaKinerja, Long id) {
        return gambaranUmumRepository.findByIdRencanaKinerjaAndId(idRencanaKinerja, id)
                .orElseThrow(() -> new GambaranUmumNotFoundException(id));
    }

    public GambaranUmum tambahGambaranUmum(GambaranUmum gambaranUmum) {

        return gambaranUmumRepository.save(gambaranUmum);
    }

    public GambaranUmum ubahGambaranUmum(Long idRencanaKinerja, Long id, GambaranUmum gambaranUmum) {
        if (!gambaranUmumRepository.findByIdRencanaKinerjaAndId(idRencanaKinerja, id).isPresent()) {
            throw new GambaranUmumNotFoundException(id);
        }

        return gambaranUmumRepository.save(gambaranUmum);
    }

    public void hapusGambaranUmum(Long id) {
        if (!gambaranUmumRepository.existsById(id)) {
            throw new GambaranUmumNotFoundException(id);
        }

        gambaranUmumRepository.deleteById(id);
    }
}