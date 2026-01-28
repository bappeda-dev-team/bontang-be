package cc.kertaskerja.bontang.laporanrincianbelanja.web.response;

import java.util.List;

public record LaporanRincianBelanjaEnvelopeResponse(
        int code,
        String status,
        List<LaporanRincianBelanjaSubkegiatanResponse> data
) {
}

