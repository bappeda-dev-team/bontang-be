package cc.kertaskerja.bontang.rincianbelanja.web.response;

import cc.kertaskerja.bontang.target.domain.Target;
import cc.kertaskerja.bontang.targetbelanja.domain.TargetBelanja;

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

    public static TargetResponse from(TargetBelanja data) {
        return new TargetResponse(
            data.id(),
            data.namaTargetBelanja(),
            data.satuan()
        );
    }
}
