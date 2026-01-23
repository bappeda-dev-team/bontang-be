package cc.kertaskerja.bontang.programprioritasanggaran.web;

public record CheckRelasiProgramPrioritasResponse(
    Long idProgramPrioritas,
    boolean hasRelation,
    long count,
    String message
) {
    public static CheckRelasiProgramPrioritasResponse of(Long idProgramPrioritas, long count) {
        boolean hasRelation = count > 0;
        String message = hasRelation
            ? "Program prioritas memiliki " + count + " relasi dengan program prioritas anggaran"
            : "Program prioritas tidak memiliki relasi dengan program prioritas anggaran";

        return new CheckRelasiProgramPrioritasResponse(idProgramPrioritas, hasRelation, count, message);
    }
}
