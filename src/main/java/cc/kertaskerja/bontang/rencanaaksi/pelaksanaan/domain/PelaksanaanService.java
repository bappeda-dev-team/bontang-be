package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain;

import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.web.PelaksanaanRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PelaksanaanService {
    private static final int TOTAL_BOBOT = 100;
    private final PelaksanaanRepository pelaksanaanRepository;

    public PelaksanaanService(PelaksanaanRepository pelaksanaanRepository) {
        this.pelaksanaanRepository = pelaksanaanRepository;
    }

    public Iterable<Pelaksanaan> findAll() {
        return pelaksanaanRepository.findAll();
    }

    public Pelaksanaan detailPelaksanaanById(Long id) {
        return pelaksanaanRepository.findById(id)
                .orElseThrow(() -> new PelaksanaanNotFoundException(id));
    }

    public Pelaksanaan tambahPelaksanaan(Integer idRencanaAksi, PelaksanaanRequest request) {
        Pelaksanaan pelaksanaan = Pelaksanaan.of(
                idRencanaAksi,
                request.bulan(),
                request.bobot()
        );
        Integer bobotTersedia = hitungBobotTersedia(pelaksanaan.idRencanaAksi(), pelaksanaan.bobot(), null);
        Pelaksanaan withBobotTersedia = pelaksanaan.withBobotTersedia(bobotTersedia);

        return pelaksanaanRepository.save(withBobotTersedia);
    }

    public Pelaksanaan ubahPelaksanaan(Integer idRencanaAksi, PelaksanaanRequest request) {
        Pelaksanaan existingPelaksanaan = pelaksanaanRepository.findByIdRencanaAksi(idRencanaAksi)
                .orElseThrow(() -> new PelaksanaanNotFoundException((long) idRencanaAksi));

        Pelaksanaan pelaksanaan = new Pelaksanaan(
                existingPelaksanaan.id(),
                idRencanaAksi,
                request.bulan(),
                request.bobot(),
                null,
                existingPelaksanaan.createdDate(),
                null
        );

        Integer bobotTersedia = hitungBobotTersedia(pelaksanaan.idRencanaAksi(), pelaksanaan.bobot(), existingPelaksanaan.id());
        Pelaksanaan withBobotTersedia = pelaksanaan.withBobotTersedia(bobotTersedia);

        return pelaksanaanRepository.save(withBobotTersedia);
    }

    public void hapusPelaksanaan(Long id) {
        if (!pelaksanaanRepository.existsById(id)) {
            throw new PelaksanaanNotFoundException(id);
        }

        pelaksanaanRepository.deleteById(id);
    }

    public void hapusPelaksanaanByIdRencanaAksi(Integer idRencanaAksi) {
        pelaksanaanRepository.deleteByIdRencanaAksi(idRencanaAksi);
    }

    private Integer hitungBobotTersedia(Integer idRencanaAksi, Integer bobot, Long excludeId) {
        int jumlahDipakai = (excludeId == null)
                ? pelaksanaanRepository.sumBobotByIdRencanaAksi(idRencanaAksi)
                : pelaksanaanRepository.sumBobotByIdRencanaAksiExcluding(idRencanaAksi, excludeId);
        int sisa = TOTAL_BOBOT - jumlahDipakai - (bobot == null ? 0 : bobot);
        return Math.max(0, sisa);
    }
}
