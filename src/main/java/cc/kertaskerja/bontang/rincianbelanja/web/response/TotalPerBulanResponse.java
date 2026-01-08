package cc.kertaskerja.bontang.rincianbelanja.web.response;

public record TotalPerBulanResponse(
    int bulan,
    int totalBobot
) {
    public static TotalPerBulanResponse create(int bulan, int totalBobot) {
        return new TotalPerBulanResponse(bulan, totalBobot);
    }
}
