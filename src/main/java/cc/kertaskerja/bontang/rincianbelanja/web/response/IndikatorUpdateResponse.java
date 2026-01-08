package cc.kertaskerja.bontang.rincianbelanja.web.response;

import java.util.List;

import cc.kertaskerja.bontang.indikatorbelanja.domain.IndikatorBelanja;
import cc.kertaskerja.bontang.targetbelanja.domain.TargetBelanja;

public record IndikatorUpdateResponse(
    Long id,
    String namaIndikator,
    List<TargetResponse> targetList
) {
    public static IndikatorUpdateResponse from(
        IndikatorBelanja indikatorBelanja,
        List<TargetBelanja> targetBelanjas
    ) {
        return new IndikatorUpdateResponse(
            indikatorBelanja.id(),
            indikatorBelanja.namaIndikatorBelanja(),
            targetBelanjas.stream().map(TargetResponse::from).toList()
        );
    }
}
