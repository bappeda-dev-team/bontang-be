package cc.kertaskerja.bontang.rencanakinerja.web.response;

import cc.kertaskerja.bontang.dasarhukum.domain.DasarHukum;

public record DasarHukumResponse(
    Long id,
    Long rencanaKinerjaId,
    String kodeOpd,
    String uraian,
    String peraturanTerkait
) {
    public static DasarHukumResponse from(DasarHukum data, Long rencanaKinerjaId) {
        return new DasarHukumResponse(
            data.id(),
            rencanaKinerjaId,
            data.kodeOpd(),
            data.uraian(),
            data.peraturanTerkait()
        );
    }
}
