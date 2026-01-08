package cc.kertaskerja.bontang.rincianbelanja.web.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cc.kertaskerja.bontang.indikatorbelanja.domain.IndikatorBelanja;
import cc.kertaskerja.bontang.targetbelanja.domain.TargetBelanja;

public record SimpleIndikatorResponse(
    Long id,
    String namaIndikator,
    List<SimpleTargetResponse> targetList
) {
    public static SimpleIndikatorResponse from(IndikatorBelanja indikator, List<TargetBelanja> targets) {
        return new SimpleIndikatorResponse(
            indikator.id(),
            indikator.namaIndikatorBelanja(),
            targets.stream()
                .map(SimpleTargetResponse::from)
                .toList()
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_indikator", id);
        map.put("nama_indikator_belanja", namaIndikator);
        map.put("targetList", targetList.stream()
            .map(SimpleTargetResponse::toMap)
            .toList());
        return map;
    }
}
