package cc.kertaskerja.bontang.subkegiatan.web.response;

import java.time.Instant;

public record SubKegiatanResponse(
        Long id,
        String kodeSubKegiatan,
        String namaSubKegiatan,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
