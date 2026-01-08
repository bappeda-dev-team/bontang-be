package cc.kertaskerja.bontang.rincianbelanja.web.response;

import java.util.List;

import cc.kertaskerja.bontang.indikator.domain.Indikator;
import cc.kertaskerja.bontang.target.domain.Target;

public record IndikatorDetailResponse(
    Long id,
    String namaIndikator,
    List<TargetResponse> targets
) {
    public static IndikatorDetailResponse from(Indikator indikator, List<Target> targets) {
        List<TargetResponse> targetResponses = targets.stream()
            .map(TargetResponse::from)
            .toList();

        return new IndikatorDetailResponse(
            indikator.id(),
            indikator.namaIndikator(),
            targetResponses
        );
    }
}
