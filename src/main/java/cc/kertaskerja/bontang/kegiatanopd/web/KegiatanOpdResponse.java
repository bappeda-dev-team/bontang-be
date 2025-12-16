package cc.kertaskerja.bontang.kegiatanopd.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record KegiatanOpdResponse(
        Long id,
        String kodeProgramOpd,
        String namaProgramOpd,
        String kodeOpd,
        Integer tahun,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
