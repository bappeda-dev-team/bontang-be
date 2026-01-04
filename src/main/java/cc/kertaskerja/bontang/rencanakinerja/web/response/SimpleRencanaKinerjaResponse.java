package cc.kertaskerja.bontang.rencanakinerja.web.response;

import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public record SimpleRencanaKinerjaResponse(
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
    String sumberDana,
    String keterangan,
    List<SimpleIndikatorResponse> indikatorList
) {
    public static SimpleRencanaKinerjaResponse from(
        RencanaKinerja rencanaKinerja,
        List<SimpleIndikatorResponse> indikatorList
    ) {
        return new SimpleRencanaKinerjaResponse(
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
            rencanaKinerja.sumberDana(),
            rencanaKinerja.keterangan(),
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
        map.put("id_sumber_dana", idSumberDana);
        map.put("sumberDana", sumberDana);
        map.put("keterangan", keterangan);
        map.put("indikatorList", indikatorList.stream()
            .map(SimpleIndikatorResponse::toMap)
            .toList());
        return map;
    }
}
