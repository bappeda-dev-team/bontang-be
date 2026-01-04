package cc.kertaskerja.bontang.rencanakinerja.web.response;

import cc.kertaskerja.bontang.target.domain.Target;
import java.util.LinkedHashMap;
import java.util.Map;

public record SimpleTargetResponse(
    Long id,
    String target,
    String satuan
) {
    public static SimpleTargetResponse from(Target data) {
        return new SimpleTargetResponse(
            data.id(),
            data.target(),
            data.satuan()
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_target", id);
        map.put("target", target);
        map.put("satuan", satuan);
        return map;
    }
}
