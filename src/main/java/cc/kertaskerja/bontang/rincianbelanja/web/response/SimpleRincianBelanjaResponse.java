package cc.kertaskerja.bontang.rincianbelanja.web.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanja;

public record SimpleRincianBelanjaResponse(
    Long id,
        Long idSumberDana,
        Long idRencanaKinerja,
        Long idRencanaAksi,
        String kodeOpd,
        Integer tahun,
        String statusRincianBelanja,
        String nipPegawai,
        String namaPegawai,
        String sumberDana,
        String rencanaKinerja,
        String rencanaAksi,
        Integer totalAnggaran,
        List<SimpleIndikatorResponse> indikatorList
) {
    public static SimpleRincianBelanjaResponse from(
        RincianBelanja rincianBelanja,
        List<SimpleIndikatorResponse> indikatorList
    ) {
        return new SimpleRincianBelanjaResponse(
            rincianBelanja.id(),
            rincianBelanja.idSumberDana(),
            rincianBelanja.idRencanaKinerja(),
            rincianBelanja.idRencanaAksi(),
            rincianBelanja.kodeOpd(),
            rincianBelanja.tahun(),
            rincianBelanja.statusRincianBelanja(),
            rincianBelanja.nipPegawai(),
            rincianBelanja.namaPegawai(),
            rincianBelanja.sumberDana(),
            rincianBelanja.rencanaKinerja(),
            rincianBelanja.rencanaAksi(),
            rincianBelanja.totalAnggaran(),
            indikatorList
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_rincian_belanja", id);
        map.put("id_sumber_dana", idSumberDana);
        map.put("id_rencana_kinerja", idRencanaKinerja);
        map.put("id_rencana_aksi", idRencanaAksi);
        map.put("kode_opd", kodeOpd);
        map.put("tahun", tahun);
        map.put("statusRincianBelanja", statusRincianBelanja);
        map.put("nip_pegawai", nipPegawai);
        map.put("nama_pegawai", namaPegawai);
        map.put("sumber_dana", sumberDana);
        map.put("rencana_kinerja", rencanaKinerja);
        map.put("rencana_aksi", rencanaAksi);
        map.put("total_anggaran", totalAnggaran);

        List<Map<String, Object>> indikatorListMap = indikatorList.stream().map(indikator -> {
            Map<String, Object> indikatorMap = new LinkedHashMap<>();
            indikatorMap.put("id_indikator", indikator.id());
            indikatorMap.put("nama_indikator", indikator.namaIndikator());
            indikatorMap.put("targets", indikator.targetList().stream().map(target -> {
                Map<String, Object> targetMap = new LinkedHashMap<>();
                targetMap.put("id_target", target.id());
                targetMap.put("target", target.target());
                targetMap.put("satuan", target.satuan());
                return targetMap;
            }).toList());
            return indikatorMap;
        }).toList();

        map.put("indikatorList", indikatorListMap);
        return map;
    }
}
