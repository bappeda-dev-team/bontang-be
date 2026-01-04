package cc.kertaskerja.bontang.rencanakinerja.web.response;

import cc.kertaskerja.bontang.target.domain.Target;

public record TargetResponse(
    Long id,
    String target,
    String satuan
) {
    public static TargetResponse from(Target data) {
        return new TargetResponse(
            data.id(),
            data.target(),
            data.satuan()
        );
    }
}
