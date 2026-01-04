package cc.kertaskerja.bontang.rencanakinerja.web.response;

import cc.kertaskerja.bontang.gambaranumum.domain.GambaranUmum;

public record GambaranUmumResponse(
    Long id,
    Long rencanaKinerjaId,
    String kodeOpd,
    String uraian,
    String gambaranUmum
) {
    public static GambaranUmumResponse from(GambaranUmum data, Long rencanaKinerjaId) {
        return new GambaranUmumResponse(
            data.id(),
            rencanaKinerjaId,
            data.kodeOpd(),
            data.uraian(),
            data.gambaranUmum()
        );
    }
}
