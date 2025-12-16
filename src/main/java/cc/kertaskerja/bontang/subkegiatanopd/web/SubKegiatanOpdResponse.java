package cc.kertaskerja.bontang.subkegiatanopd.web;

import java.time.Instant;

public record SubKegiatanOpdResponse(
        Long id,
        String kodeSubKegiatanOpd,
        String namaSubKegiatanOpd,
        String kodeOpd,
        Integer tahun,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
