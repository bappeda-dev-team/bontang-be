package cc.kertaskerja.bontang.kegiatan.web.response;

import java.time.Instant;

public record KegiatanResponse(
        Long id,
        String kodeKegiatan,
        String namaKegiatan,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
