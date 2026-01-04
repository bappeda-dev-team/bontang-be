package cc.kertaskerja.bontang.rencanakinerja.web.response;

import cc.kertaskerja.bontang.indikator.domain.Indikator;
import cc.kertaskerja.bontang.target.domain.Target;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public record SimpleIndikatorResponse(
    Long id,
    String namaIndikator,
    List<SimpleTargetResponse> targetList
) {
    public static SimpleIndikatorResponse from(Indikator indikator, List<Target> targets) {
        return new SimpleIndikatorResponse(
            indikator.id(),
            indikator.namaIndikator(),
            targets.stream()
                .map(SimpleTargetResponse::from)
                .toList()
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_indikator", id);
        map.put("namaIndikator", namaIndikator);
        map.put("targetList", targetList.stream()
            .map(SimpleTargetResponse::toMap)
            .toList());
        return map;
    }
}
