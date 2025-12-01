package cc.kertaskerja.bontang.kegiatan.web.response;

import java.time.Instant;

public record KegiatanResponse(
        Long id,
        String kodeKegiatan,
        String namaKegiatan,
        String kodeProgram,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
