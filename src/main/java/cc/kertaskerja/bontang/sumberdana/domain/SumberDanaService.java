package cc.kertaskerja.bontang.sumberdana.domain;

import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
import cc.kertaskerja.bontang.sumberdana.domain.exception.SumberDanaNotFoundException;
import cc.kertaskerja.bontang.sumberdana.web.SumberDanaRequest;
import org.springframework.stereotype.Service;

@Service
public class SumberDanaService {
    private SumberDanaRepository sumberDanaRepository;
    private RencanaKinerjaRepository rencanaKinerjaRepository;

    public SumberDanaService(SumberDanaRepository sumberDanaRepository, RencanaKinerjaRepository rencanaKinerjaRepository) {
        this.sumberDanaRepository = sumberDanaRepository;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
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

    public SumberDana tambahSumberDanaFromRequest(SumberDanaRequest request) {
        SumberDana sumberDana = SumberDana.of(
                request.kodeDanaLama(),
                request.sumberDana(),
                request.kodeDanaBaru(),
                request.setInput()
        );
        return sumberDanaRepository.save(sumberDana);
    }

    public SumberDana ubahSumberDana(Long id, SumberDana sumberDana) {
        if (!sumberDanaRepository.existsById(id)) {
            throw new SumberDanaNotFoundException(id);
        }

        return sumberDanaRepository.save(sumberDana);
    }

    public SumberDana ubahSumberDanaFromRequest(Long id, SumberDanaRequest request) {
        SumberDana existingSumberDana = detailSumberDanaById(id);

        SumberDana sumberDana = new SumberDana(
                existingSumberDana.id(),
                request.kodeDanaLama(),
                request.sumberDana(),
                request.kodeDanaBaru(),
                request.setInput(),
                existingSumberDana.createdDate(),
                null
        );

        return sumberDanaRepository.save(sumberDana);
    }

    public void hapusSumberDana(Long id) {
        if (!sumberDanaRepository.existsById(id)) {
            throw new SumberDanaNotFoundException(id);
        }

        // Set id_sumber_dana dan sumber_dana ke null di tabel rencana_kinerja
        rencanaKinerjaRepository.updateSumberDanaToNullByIdSumberDana(id);

        sumberDanaRepository.deleteById(id);
    }
}
