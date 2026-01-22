package cc.kertaskerja.bontang.programprioritasanggaran.web;

import java.time.Instant;
import java.util.List;

public record ProgramPrioritasAnggaranWithRencanaKinerjaResponse(
        Long id,
        Long idProgramPrioritas,
        String kodeOpd,
        String nip,
        Integer tahun,
        List<RencanaKinerjaItem> rencanaKinerja,
        Instant createdDate,
        Instant lastModifiedDate
) {
    public record RencanaKinerjaItem(
            Long id,
            String namaRencanaKinerja
    ) {}
}
