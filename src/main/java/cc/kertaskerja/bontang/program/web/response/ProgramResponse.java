package cc.kertaskerja.bontang.program.web.response;

import java.time.Instant;

public record ProgramResponse(
        Long id,
        String kodeProgram,
        String namaProgram,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
