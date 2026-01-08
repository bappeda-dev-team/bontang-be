package cc.kertaskerja.bontang.rincianbelanja.web.response;

import java.util.List;
import java.util.Map;

import cc.kertaskerja.bontang.rinciananggaran.domain.RincianAnggaran;

public record RincianAnggaranResponse(
    Long id,
    Long rincianBelanjaId,
    String namaRincianBelanja,
    Integer urutan,
    List<KodeRekeningResponse> kodeRekening,
    List<Map<String, Object>> pelaksanaan,
    int jumlahBobot
) {
    public static RincianAnggaranResponse from(
        RincianAnggaran rincianAnggaran,
        Long rincianBelanjaId,
        List<KodeRekeningResponse> kodeRekeningList,
        List<Map<String, Object>> pelaksanaanList,
        int jumlahBobot
    ) {
        return new RincianAnggaranResponse(
            rincianAnggaran.id(),
            rincianBelanjaId,
            rincianAnggaran.namaRincianAnggaran(),
            rincianAnggaran.urutan(),
            kodeRekeningList,
            pelaksanaanList,
            jumlahBobot
        );
    }
}
