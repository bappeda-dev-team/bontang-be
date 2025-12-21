package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain;

import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.web.PelaksanaanRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

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
        // Hitung bobot_tersedia dari entitas yang sudah ada
        // Logic: ambil entitas dengan bobot_tersedia terkecil, kemudian kurangi dengan bobot baru
        Integer bobotTersediaSaatIni = (excludeId == null)
                ? pelaksanaanRepository.findMinBobotTersediaByIdRencanaAksi(idRencanaAksi, TOTAL_BOBOT)
                : pelaksanaanRepository.findBobotTersediaById(idRencanaAksi, excludeId, TOTAL_BOBOT);

        // Kurangi bobot_tersedia dengan bobot yang baru
        int sisa = bobotTersediaSaatIni - (bobot == null ? 0 : bobot);
        return Math.max(0, sisa);
    }

    public Iterable<Pelaksanaan> findByIdRencanaAksiOrderByBulan(Integer idRencanaAksi) {
        return pelaksanaanRepository.findByIdRencanaAksiOrderByBulan(idRencanaAksi);
    }

    public List<Map<String, Object>> buildPelaksanaanResponseList(Integer idRencanaAksi) {
        List<Map<String, Object>> pelaksanaanList = new ArrayList<>();
        Iterable<Pelaksanaan> pelaksanaans = findByIdRencanaAksiOrderByBulan(idRencanaAksi);

        Map<Integer, Pelaksanaan> pelaksanaanMap = createPelaksanaanMap(pelaksanaans);

        for (int month = 1; month <= 12; month++) {
            Map<String, Object> pelaksanaanResponse = new LinkedHashMap<>();
            Pelaksanaan pelaksanaan = pelaksanaanMap.get(month);

            if (pelaksanaan != null) {
                pelaksanaanResponse.put("id", pelaksanaan.id());
                pelaksanaanResponse.put("rencana_aksi_id", idRencanaAksi);
                pelaksanaanResponse.put("bulan", pelaksanaan.bulan());
                pelaksanaanResponse.put("bobot", pelaksanaan.bobot());
            } else {
                pelaksanaanResponse.put("id", 0);
                pelaksanaanResponse.put("rencana_aksi_id", idRencanaAksi);
                pelaksanaanResponse.put("bulan", month);
                pelaksanaanResponse.put("bobot", 0);
            }
            pelaksanaanList.add(pelaksanaanResponse);
        }

        return pelaksanaanList;
    }

    private Map<Integer, Pelaksanaan> createPelaksanaanMap(Iterable<Pelaksanaan> pelaksanaanList) {
        Map<Integer, Pelaksanaan> pelaksanaanMap = new LinkedHashMap<>();
        for (Pelaksanaan pelaksanaan : pelaksanaanList) {
            pelaksanaanMap.put(pelaksanaan.bulan(), pelaksanaan);
        }
        return pelaksanaanMap;
    }
}
