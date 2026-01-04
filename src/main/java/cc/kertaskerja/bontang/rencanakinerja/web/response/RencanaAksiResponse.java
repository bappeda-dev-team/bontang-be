package cc.kertaskerja.bontang.rencanakinerja.web.response;

import java.util.List;
import java.util.Map;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi;

public record RencanaAksiResponse(
    Long id,
    Long rencanaKinerjaId,
    Integer urutan,
    String namaRencanaAksi,
    List<Map<String, Object>> pelaksanaan,
    int jumlahBobot
) {
    public static RencanaAksiResponse from(
        RencanaAksi rencanaAksi,
        Long rencanaKinerjaId,
        List<Map<String, Object>> pelaksanaanList,
        int jumlahBobot
    ) {
        return new RencanaAksiResponse(
            rencanaAksi.id(),
            rencanaKinerjaId,
            rencanaAksi.urutan(),
            rencanaAksi.namaRencanaAksi(),
            pelaksanaanList,
            jumlahBobot
        );
    }
}
