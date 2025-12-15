package cc.kertaskerja.bontang.subkegiatan.web.response;

import java.time.Instant;

public record SubKegiatanResponse(
        Long id,
        String kodeSubKegiatan,
        String namaSubKegiatan,
        String kodeOpd,
        Integer tahun,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
