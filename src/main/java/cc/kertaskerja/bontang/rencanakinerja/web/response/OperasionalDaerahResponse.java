package cc.kertaskerja.bontang.rencanakinerja.web.response;

public record OperasionalDaerahResponse(
    String kodeOpd,
    String namaOpd
) {
    public static OperasionalDaerahResponse from(String kodeOpd, String namaOpd) {
        return new OperasionalDaerahResponse(kodeOpd, namaOpd);
    }
}
