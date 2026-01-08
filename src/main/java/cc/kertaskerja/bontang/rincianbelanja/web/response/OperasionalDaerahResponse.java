package cc.kertaskerja.bontang.rincianbelanja.web.response;

public record OperasionalDaerahResponse(
    String kodeOpd,
    String namaOpd
) {
    public static OperasionalDaerahResponse from(String kodeOpd, String namaOpd) {
        return new OperasionalDaerahResponse(kodeOpd, namaOpd);
    }
}
