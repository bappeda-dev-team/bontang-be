package cc.kertaskerja.bontang.programopd.web;

import java.time.Instant;

public record ProgramOpdResponse(
        Long id,
        String kodeProgramOpd,
        String namaProgramOpd,
        String kodeOpd,
        Integer tahun,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
