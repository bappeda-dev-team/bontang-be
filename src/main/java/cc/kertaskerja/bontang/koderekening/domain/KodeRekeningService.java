package cc.kertaskerja.bontang.koderekening.domain;

import cc.kertaskerja.bontang.koderekening.domain.exception.KodeRekeningNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class KodeRekeningService {
    private KodeRekeningRepository kodeRekeningRepository;

    public KodeRekeningService(KodeRekeningRepository kodeRekeningRepository) {
        this.kodeRekeningRepository = kodeRekeningRepository;
    }

    public Iterable<KodeRekening> findAll() {
        return kodeRekeningRepository.findAll();
    }

    public KodeRekening detailKodeRekeningByKodeRekening(String kodeRekening) {
        return kodeRekeningRepository.findByKodeRekening(kodeRekening)
                .orElseThrow(() -> new KodeRekeningNotFoundException(kodeRekening));
    }

    public KodeRekening tambahKodeRekening(KodeRekening KodeRekening) {

        return kodeRekeningRepository.save(KodeRekening);
    }

    public KodeRekening ubahKodeRekening(String kodeRekening, KodeRekening KodeRekening) {
        if (!kodeRekeningRepository.existsByKodeRekening(kodeRekening)) {
            throw new KodeRekeningNotFoundException(kodeRekening);
        }

        return kodeRekeningRepository.save(KodeRekening);
    }

    public void hapusKodeRekening(String kodeRekening) {
        if (!kodeRekeningRepository.existsByKodeRekening(kodeRekening)) {
            throw new KodeRekeningNotFoundException(kodeRekening);
        }

        kodeRekeningRepository.deleteByKodeRekening(kodeRekening);
    }
}
