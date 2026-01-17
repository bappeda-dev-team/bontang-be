package cc.kertaskerja.bontang.rencanakinerja.web.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;

public record RencanaKinerjaDetailResponse(
    Long id,
    String namaRencanaKinerja,
    String tahun,
    String statusRencanaKinerja,
    Long idSumberDana,
    OperasionalDaerahResponse operasionalDaerah,
    String nip,
    String namaPegawai,
    List<IndikatorDetailResponse> indikator,
    List<SubkegiatanResponse> subkegiatan,
    List<DasarHukumResponse> dasarHukum,
    List<GambaranUmumResponse> gambaranUmum,
    List<RencanaAksiResponse> rencanaAksi,
    List<TotalPerBulanResponse> totalPerBulan,
    int totalKeseluruhan,
    int waktuDibutuhkan
) {
    public static RencanaKinerjaDetailResponse from(
        RencanaKinerja rencanaKinerja,
        List<IndikatorDetailResponse> indikatorList,
        List<SubkegiatanResponse> subkegiatanList,
        List<DasarHukumResponse> dasarHukumList,
        List<GambaranUmumResponse> gambaranUmumList,
        List<RencanaAksiResponse> rencanaAksiList,
        List<TotalPerBulanResponse> totalPerBulanList,
        int totalKeseluruhan,
        int waktuDibutuhkan
    ) {
        OperasionalDaerahResponse operasionalDaerah = OperasionalDaerahResponse.from(
            rencanaKinerja.kodeOpd(),
            rencanaKinerja.namaOpd()
        );

        return new RencanaKinerjaDetailResponse(
            rencanaKinerja.id(),
            rencanaKinerja.rencanaKinerja(),
            rencanaKinerja.tahun().toString(),
            rencanaKinerja.statusRencanaKinerja(),
            rencanaKinerja.idSumberDana(),
            operasionalDaerah,
            rencanaKinerja.nipPegawai(),
            rencanaKinerja.namaPegawai(),
            indikatorList,
            subkegiatanList,
            dasarHukumList,
            gambaranUmumList,
            rencanaAksiList,
            totalPerBulanList,
            totalKeseluruhan,
            waktuDibutuhkan
        );
    }

    // Convert to Map for backward compatibility
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_rencana_kinerja", id);
        map.put("namaRencanaKinerja", namaRencanaKinerja);
        map.put("tahun", tahun);
        map.put("statusRencanaKinerja", statusRencanaKinerja);
        map.put("id_sumber_dana", idSumberDana);

        Map<String, Object> operasionalDaerahMap = new LinkedHashMap<>();
        operasionalDaerahMap.put("kodeOpd", operasionalDaerah.kodeOpd());
        operasionalDaerahMap.put("namaOpd", operasionalDaerah.namaOpd());
        map.put("operasionalDaerah", operasionalDaerahMap);

        map.put("nip", nip);
        map.put("namaPegawai", namaPegawai);
        map.put("indikator", indikator.stream().map(this::indikatorToMap).toList());
        map.put("subKegiatan", subkegiatan.stream().map(this::subkegiatanToMap).toList());
        map.put("dasarHukum", dasarHukum.stream().map(this::dasarHukumToMap).toList());
        map.put("gambaranUmum", gambaranUmum.stream().map(this::gambaranUmumToMap).toList());
        map.put("rencanaAksi", rencanaAksi.stream().map(this::rencanaAksiToMap).toList());
        map.put("totalPerBulan", totalPerBulan.stream().map(this::totalPerBulanToMap).toList());
        map.put("totalKeseluruhan", totalKeseluruhan);
        map.put("waktuDibutuhkan", waktuDibutuhkan);

        return map;
    }

    private Map<String, Object> indikatorToMap(IndikatorDetailResponse indikator) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_indikator", indikator.id());
        map.put("namaIndikator", indikator.namaIndikator());
        map.put("targets", indikator.targets().stream().map(t -> {
            Map<String, Object> targetMap = new LinkedHashMap<>();
            targetMap.put("id_target", t.id());
            targetMap.put("target", t.target());
            targetMap.put("satuan", t.satuan());
            return targetMap;
        }).toList());
        return map;
    }

    private Map<String, Object> subkegiatanToMap(SubkegiatanResponse subkegiatan) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", subkegiatan.id());
        map.put("rencana_kinerja_id", subkegiatan.rencanaKinerjaId());
        map.put("kodeSubKegiatan", subkegiatan.kodeSubKegiatan());
        map.put("namaSubKegiatan", subkegiatan.namaSubKegiatan());
        return map;
    }

    private Map<String, Object> dasarHukumToMap(DasarHukumResponse dasarHukum) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", dasarHukum.id());
        map.put("rencana_kinerja_id", dasarHukum.rencanaKinerjaId());
        map.put("kodeOpd", dasarHukum.kodeOpd());
        map.put("uraian", dasarHukum.uraian());
        map.put("peraturanTerkait", dasarHukum.peraturanTerkait());
        return map;
    }

    private Map<String, Object> gambaranUmumToMap(GambaranUmumResponse gambaranUmum) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", gambaranUmum.id());
        map.put("rencana_kinerja_id", gambaranUmum.rencanaKinerjaId());
        map.put("kodeOpd", gambaranUmum.kodeOpd());
        map.put("uraian", gambaranUmum.uraian());
        map.put("gambaranUmum", gambaranUmum.gambaranUmum());
        return map;
    }

    private Map<String, Object> rencanaAksiToMap(RencanaAksiResponse rencanaAksi) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", rencanaAksi.id());
        map.put("rencana_kinerja_id", rencanaAksi.rencanaKinerjaId());
        map.put("urutan", rencanaAksi.urutan());
        map.put("namaRencanaAksi", rencanaAksi.namaRencanaAksi());
        map.put("pelaksanaan", rencanaAksi.pelaksanaan());
        map.put("jumlahBobot", rencanaAksi.jumlahBobot());
        map.put("sisaBobot", rencanaAksi.sisaBobot());
        return map;
    }

    private Map<String, Object> totalPerBulanToMap(TotalPerBulanResponse total) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("bulan", total.bulan());
        map.put("totalBobot", total.totalBobot());
        return map;
    }
}
