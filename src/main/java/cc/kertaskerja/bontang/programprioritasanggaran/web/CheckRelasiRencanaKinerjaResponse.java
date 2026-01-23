package cc.kertaskerja.bontang.programprioritasanggaran.web;

public record CheckRelasiRencanaKinerjaResponse(
    Long idRencanaKinerja,
    boolean hasRelation,
    long count,
    String message
) {
    public static CheckRelasiRencanaKinerjaResponse of(Long idRencanaKinerja, long count) {
        boolean hasRelation = count > 0;
        String message = hasRelation
            ? "Rencana kinerja memiliki " + count + " relasi dengan program prioritas anggaran"
            : "Rencana kinerja tidak memiliki relasi dengan program prioritas anggaran";

        return new CheckRelasiRencanaKinerjaResponse(idRencanaKinerja, hasRelation, count, message);
    }
}
