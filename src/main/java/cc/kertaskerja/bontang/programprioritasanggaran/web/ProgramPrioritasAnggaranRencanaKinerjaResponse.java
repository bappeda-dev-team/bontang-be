package cc.kertaskerja.bontang.programprioritasanggaran.web;

import java.time.Instant;

public record ProgramPrioritasAnggaranRencanaKinerjaResponse(
        Long id,
        Long idProgramPrioritasAnggaran,
        Long idRencanaKinerja,
        String namaRencanaKinerja,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
