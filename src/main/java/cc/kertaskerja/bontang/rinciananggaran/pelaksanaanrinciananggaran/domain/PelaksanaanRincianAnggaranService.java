package cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.domain;

import cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.domain.exception.PelaksanaanRincianAnggaranNotFoundException;
import cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.web.PelaksanaanRincianAnggaranRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PelaksanaanRincianAnggaranService {
    private static final int TOTAL_BOBOT = 100;
    private final PelaksanaanRincianAnggaranRepository pelaksanaanRincianAnggaranRepository;

    public PelaksanaanRincianAnggaranService(PelaksanaanRincianAnggaranRepository pelaksanaanRincianAnggaranRepository) {
        this.pelaksanaanRincianAnggaranRepository = pelaksanaanRincianAnggaranRepository;
    }

    public Iterable<PelaksanaanRincianAnggaran> findAll() {
        return pelaksanaanRincianAnggaranRepository.findAll();
    }

    public PelaksanaanRincianAnggaran detailPelaksanaanRincianAnggaranById(Long id) {
        return pelaksanaanRincianAnggaranRepository.findById(id)
                .orElseThrow(() -> new PelaksanaanRincianAnggaranNotFoundException(id));
    }

    public PelaksanaanRincianAnggaran tambahPelaksanaanRincianAnggaran(Integer idRincianAnggaran, PelaksanaanRincianAnggaranRequest request) {
        PelaksanaanRincianAnggaran pelaksanaanRincianAnggaran = PelaksanaanRincianAnggaran.of(
                idRincianAnggaran,
                request.bulan(),
                request.bobot()
        );

        return pelaksanaanRincianAnggaranRepository.save(pelaksanaanRincianAnggaran);
    }

    public PelaksanaanRincianAnggaran ubahPelaksanaanRincianAnggaran(Integer idRincianAnggaran, PelaksanaanRincianAnggaranRequest request) {
        PelaksanaanRincianAnggaran existingPelaksanaanRincianAnggaran = pelaksanaanRincianAnggaranRepository.findByIdRincianAnggaran(idRincianAnggaran)
                .orElseThrow(() -> new PelaksanaanRincianAnggaranNotFoundException(0L));

        PelaksanaanRincianAnggaran pelaksanaanRincianAnggaran = new PelaksanaanRincianAnggaran(
                existingPelaksanaanRincianAnggaran.id(),
                idRincianAnggaran,
                request.bulan(),
                request.bobot(),
                existingPelaksanaanRincianAnggaran.createdDate(),
                null
        );

        return pelaksanaanRincianAnggaranRepository.save(pelaksanaanRincianAnggaran);
    }

    public PelaksanaanRincianAnggaran ubahPelaksanaanRincianAnggaranById(Long id, PelaksanaanRincianAnggaranRequest request) {
        PelaksanaanRincianAnggaran existingPelaksanaanRincianAnggaran = pelaksanaanRincianAnggaranRepository.findById(id)
                .orElseThrow(() -> new PelaksanaanRincianAnggaranNotFoundException(id));

        PelaksanaanRincianAnggaran pelaksanaanRincianAnggaran = new PelaksanaanRincianAnggaran(
                existingPelaksanaanRincianAnggaran.id(),
                existingPelaksanaanRincianAnggaran.idRincianAnggaran(),
                request.bulan(),
                request.bobot(),
                existingPelaksanaanRincianAnggaran.createdDate(),
                null
        );

        return pelaksanaanRincianAnggaranRepository.save(pelaksanaanRincianAnggaran);
    }

    public void hapusPelaksanaanRincianAnggaran(Long id) {
        if (!pelaksanaanRincianAnggaranRepository.existsById(id)) {
            throw new PelaksanaanRincianAnggaranNotFoundException(id);
        }

        pelaksanaanRincianAnggaranRepository.deleteById(id);
    }

    public void hapusPelaksanaanRincianAnggaranByIdRincianAnggaran(Integer idRincianAnggaran) {
        pelaksanaanRincianAnggaranRepository.deleteByIdRincianAnggaran(idRincianAnggaran);
    }

    public Iterable<PelaksanaanRincianAnggaran> findByIdRincianAnggaranOrderByBulan(Integer idRincianAnggaran) {
        return pelaksanaanRincianAnggaranRepository.findByIdRincianAnggaranOrderByBulan(idRincianAnggaran);
    }

    public List<Map<String, Object>> buildPelaksanaanRincianAnggaranResponseList(Integer idRincianAnggaran) {
        List<Map<String, Object>> pelaksanaanRincianAnggaranList = new ArrayList<>();
        Iterable<PelaksanaanRincianAnggaran> pelaksanaanRincianAnggarans = findByIdRincianAnggaranOrderByBulan(idRincianAnggaran);

        Map<Integer, PelaksanaanRincianAnggaran> pelaksanaanRincianAnggaranMap = createPelaksanaanRincianAnggaranMap(pelaksanaanRincianAnggarans);

        for (int month = 1; month <= 12; month++) {
            Map<String, Object> pelaksanaanRincianAnggaranResponse = new LinkedHashMap<>();
            PelaksanaanRincianAnggaran pelaksanaanRincianAnggaran = pelaksanaanRincianAnggaranMap.get(month);

            if (pelaksanaanRincianAnggaran != null) {
                pelaksanaanRincianAnggaranResponse.put("id", pelaksanaanRincianAnggaran.id());
                pelaksanaanRincianAnggaranResponse.put("rincian_anggaran_id", idRincianAnggaran);
                pelaksanaanRincianAnggaranResponse.put("bulan", pelaksanaanRincianAnggaran.bulan());
                pelaksanaanRincianAnggaranResponse.put("bobot", pelaksanaanRincianAnggaran.bobot());
            } else {
                pelaksanaanRincianAnggaranResponse.put("id", 0);
                pelaksanaanRincianAnggaranResponse.put("rincian_anggaran_id", idRincianAnggaran);
                pelaksanaanRincianAnggaranResponse.put("bulan", month);
                pelaksanaanRincianAnggaranResponse.put("bobot", 0);
            }
            pelaksanaanRincianAnggaranList.add(pelaksanaanRincianAnggaranResponse);
        }

        return pelaksanaanRincianAnggaranList;
    }

    private Map<Integer, PelaksanaanRincianAnggaran> createPelaksanaanRincianAnggaranMap(Iterable<PelaksanaanRincianAnggaran> pelaksanaanRincianAnggaranList) {
        Map<Integer, PelaksanaanRincianAnggaran> pelaksanaanRincianAnggaranMap = new LinkedHashMap<>();
        Iterator<PelaksanaanRincianAnggaran> iterator = pelaksanaanRincianAnggaranList.iterator();
        while (iterator.hasNext()) {
            PelaksanaanRincianAnggaran pelaksanaanRincianAnggaran = iterator.next();
            pelaksanaanRincianAnggaranMap.put(pelaksanaanRincianAnggaran.bulan(), pelaksanaanRincianAnggaran);
        }
        return pelaksanaanRincianAnggaranMap;
    }
}