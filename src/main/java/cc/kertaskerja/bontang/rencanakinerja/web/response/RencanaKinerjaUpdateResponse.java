package cc.kertaskerja.bontang.rencanakinerja.web.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;

public record RencanaKinerjaUpdateResponse(
    Long id,
    Long idSumberDana,
    String rencanaKinerja,
    String kodeOpd,
    String nipPegawai,
    String createdBy,
    Integer tahun,
    String statusRencanaKinerja,
    String namaOpd,
    String namaPegawai,
    String keterangan,
    List<IndikatorUpdateResponse> indikatorList
) {
    public static RencanaKinerjaUpdateResponse from(
        RencanaKinerja rencanaKinerja,
        List<IndikatorUpdateResponse> indikatorList
    ) {
        return new RencanaKinerjaUpdateResponse(
            rencanaKinerja.id(),
            rencanaKinerja.idSumberDana(),
            rencanaKinerja.rencanaKinerja(),
            rencanaKinerja.kodeOpd(),
            rencanaKinerja.nipPegawai(),
            rencanaKinerja.createdBy(),
            rencanaKinerja.tahun(),
            rencanaKinerja.statusRencanaKinerja(),
            rencanaKinerja.namaOpd(),
            rencanaKinerja.namaPegawai(),
            rencanaKinerja.keterangan(),
            indikatorList
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_rencana_kinerja", id);
        map.put("idSumberDana", idSumberDana);
        map.put("rencanaKinerja", rencanaKinerja);
        map.put("kodeOpd", kodeOpd);
        map.put("nipPegawai", nipPegawai);
        map.put("createdBy", createdBy);
        map.put("tahun", tahun);
        map.put("statusRencanaKinerja", statusRencanaKinerja);
        map.put("namaOpd", namaOpd);
        map.put("namaPegawai", namaPegawai);
        map.put("keterangan", keterangan);

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
