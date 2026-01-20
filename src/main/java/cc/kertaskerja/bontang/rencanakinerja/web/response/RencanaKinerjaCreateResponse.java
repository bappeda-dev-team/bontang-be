package cc.kertaskerja.bontang.rencanakinerja.web.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;

public record RencanaKinerjaCreateResponse(
    Long id,
    String rencanaKinerja,
    String kodeOpd,
    String nipPegawai,
    String createdBy,
    Integer tahun,
    String statusRencanaKinerja,
    String namaOpd,
    String namaPegawai,
    String keterangan,
    List<SumberDanaResponse> sumberDanaList,
    List<IndikatorCreateResponse> indikatorList
) {
    public static RencanaKinerjaCreateResponse from(
        RencanaKinerja rencanaKinerja,
        List<SumberDanaResponse> sumberDanaList,
        List<IndikatorCreateResponse> indikatorList
    ) {
        return new RencanaKinerjaCreateResponse(
            rencanaKinerja.id(),
            rencanaKinerja.rencanaKinerja(),
            rencanaKinerja.kodeOpd(),
            rencanaKinerja.nipPegawai(),
            rencanaKinerja.createdBy(),
            rencanaKinerja.tahun(),
            rencanaKinerja.statusRencanaKinerja(),
            rencanaKinerja.namaOpd(),
            rencanaKinerja.namaPegawai(),
            rencanaKinerja.keterangan(),
            sumberDanaList,
            indikatorList
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_rencana_kinerja", id);
        map.put("rencanaKinerja", rencanaKinerja);
        map.put("kodeOpd", kodeOpd);
        map.put("nipPegawai", nipPegawai);
        map.put("createdBy", createdBy);
        map.put("tahun", tahun);
        map.put("statusRencanaKinerja", statusRencanaKinerja);
        map.put("namaOpd", namaOpd);
        map.put("namaPegawai", namaPegawai);
        map.put("keterangan", keterangan);
        map.put("sumberDanaList", sumberDanaList.stream()
            .map(SumberDanaResponse::toMap)
            .toList());

        List<Map<String, Object>> indikatorListMap = indikatorList.stream().map(indikator -> {
            Map<String, Object> indikatorMap = new LinkedHashMap<>();
            indikatorMap.put("id_indikator", indikator.id());
            indikatorMap.put("namaIndikator", indikator.namaIndikator());
            indikatorMap.put("targetList", indikator.targetList().stream().map(target -> {
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
