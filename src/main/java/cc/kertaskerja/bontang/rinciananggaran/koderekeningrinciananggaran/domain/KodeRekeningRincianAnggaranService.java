package cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain;

import cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain.exception.KodeRekeningRincianAnggaranNotFoundException;
import cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.web.KodeRekeningRincianAnggaranRequest;
import org.springframework.stereotype.Service;

@Service
public class KodeRekeningRincianAnggaranService {
    private final KodeRekeningRincianAnggaranRepository kodeRekeningRincianAnggaranRepository;

    public KodeRekeningRincianAnggaranService(KodeRekeningRincianAnggaranRepository kodeRekeningRincianAnggaranRepository) {
        this.kodeRekeningRincianAnggaranRepository = kodeRekeningRincianAnggaranRepository;
    }

    public Iterable<KodeRekeningRincianAnggaran> findAll() {
        return kodeRekeningRincianAnggaranRepository.findAll();
    }

    public Iterable<KodeRekeningRincianAnggaran> findByIdRincianAnggaran(Integer idRincianAnggaran) {
        return kodeRekeningRincianAnggaranRepository.findByIdRincianAnggaran(Long.valueOf(idRincianAnggaran));
    }

    public KodeRekeningRincianAnggaran detailKodeRekeningRincianAnggaranById(Long id) {
        return kodeRekeningRincianAnggaranRepository.findById(id)
                .orElseThrow(() -> new KodeRekeningRincianAnggaranNotFoundException(id));
    }

    public KodeRekeningRincianAnggaran tambahKodeRekeningRincianAnggaran(Integer idRincianAnggaran, KodeRekeningRincianAnggaranRequest request) {
        KodeRekeningRincianAnggaran kodeRekeningRincianAnggaran = KodeRekeningRincianAnggaran.of(
                idRincianAnggaran,
                request.idKodeRekening(),
                request.kodeRekening(),
                request.namaRekening()
        );

        return kodeRekeningRincianAnggaranRepository.save(kodeRekeningRincianAnggaran);
    }

    public KodeRekeningRincianAnggaran ubahKodeRekeningRincianAnggaran(Long id, KodeRekeningRincianAnggaranRequest request) {
        KodeRekeningRincianAnggaran existingKodeRekening = kodeRekeningRincianAnggaranRepository.findById(id)
                .orElseThrow(() -> new KodeRekeningRincianAnggaranNotFoundException(id));

        KodeRekeningRincianAnggaran updatedKodeRekening = new KodeRekeningRincianAnggaran(
                id,
                existingKodeRekening.idRincianAnggaran(),
                request.idKodeRekening() != null ? request.idKodeRekening() : existingKodeRekening.idKodeRekening(),
                request.kodeRekening(),
                request.namaRekening(),
                existingKodeRekening.createdDate(),
                existingKodeRekening.lastModifiedDate()
        );

        return kodeRekeningRincianAnggaranRepository.save(updatedKodeRekening);
    }

    public void hapusKodeRekeningRincianAnggaran(Long id) {
        if (!kodeRekeningRincianAnggaranRepository.findById(id).isPresent()) {
            throw new KodeRekeningRincianAnggaranNotFoundException(id);
        }

        kodeRekeningRincianAnggaranRepository.deleteById(id);
    }
}