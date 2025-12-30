package cc.kertaskerja.bontang.kegiatanopd.web;

import java.time.Instant;

public record KegiatanOpdResponse(
        Long id,
        String kodeKegiatanOpd,
        String namaKegiatanOpd,
        String kodeOpd,
        Integer tahun,
        Instant createdDate,
        Instant lastModifiedDate
) {
}
