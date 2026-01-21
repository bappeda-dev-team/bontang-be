package cc.kertaskerja.bontang.programprioritasanggaran.web;

import java.time.Instant;
import java.util.List;

public record ProgramPrioritasAnggaranResponse(
        Long id,
        Long idProgramPrioritas,
        String programPrioritas,
        String kodeOpd,
        String namaOpd,
        List<RencanaKinerjaSimpleResponse> rencanaKinerjaList,
        Instant createdDate,
        Instant lastModifiedDate
) {
    public record RencanaKinerjaSimpleResponse(
            Long id,
            String namaRencanaKinerja
    ) {}
}
