package cc.kertaskerja.bontang.laporanrincianbelanja.web.response;

import java.util.List;

public record LaporanRincianBelanjaAllOpdResponse(
        int code,
        String status,
        List<LaporanRincianBelanjaOpdResponse> data
) {
}
