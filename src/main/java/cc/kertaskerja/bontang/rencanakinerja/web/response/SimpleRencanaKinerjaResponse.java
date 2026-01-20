package cc.kertaskerja.bontang.rencanakinerja.web.response;

import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public record SimpleRencanaKinerjaResponse(
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
    List<SimpleIndikatorResponse> indikatorList
) {
    public static SimpleRencanaKinerjaResponse from(
        RencanaKinerja rencanaKinerja,
        List<SumberDanaResponse> sumberDanaList,
        List<SimpleIndikatorResponse> indikatorList
    ) {
        return new SimpleRencanaKinerjaResponse(
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
        map.put("sumberDanaList", sumberDanaList.stream()
            .map(SumberDanaResponse::toMap)
            .toList());
        map.put("keterangan", keterangan);
        map.put("indikatorList", indikatorList.stream()
            .map(SimpleIndikatorResponse::toMap)
            .toList());
        return map;
    }
}
