package cc.kertaskerja.bontang.laporanrincianbelanja.web.response;

import java.util.List;

public record LaporanRincianBelanjaOpdResponse(
        String kodeOpd,
        String namaOpd,
        List<LaporanRincianBelanjaSubkegiatanResponse> data
) {
}
