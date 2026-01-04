package cc.kertaskerja.bontang.rencanakinerja.web.response;

import java.util.List;
import cc.kertaskerja.bontang.indikator.domain.Indikator;
import cc.kertaskerja.bontang.target.domain.Target;

public record IndikatorUpdateResponse(
    Long id,
    String namaIndikator,
    List<TargetResponse> targetList
) {
    public static IndikatorUpdateResponse from(
        Indikator indikator,
        List<Target> targets
    ) {
        return new IndikatorUpdateResponse(
            indikator.id(),
            indikator.namaIndikator(),
            targets.stream().map(TargetResponse::from).toList()
        );
    }
}
