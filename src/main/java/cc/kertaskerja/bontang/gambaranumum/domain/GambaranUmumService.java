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

    public GambaranUmum tambahGambaranUmum(GambaranUmum gambaranUmum) {

        return gambaranUmumRepository.save(gambaranUmum);
    }

    public GambaranUmum ubahGambaranUmum(Long id, GambaranUmum gambaranUmum) {
        if (!gambaranUmumRepository.existsById(id)) {
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