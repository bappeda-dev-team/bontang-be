package cc.kertaskerja.bontang.laporanverifikasi.web.response;

import java.time.Instant;

public record LaporanCetakResponse(
        String jenisLaporan,
        String kodeOpd,
        Integer tahun,
        String filterHash,
        String verifiedByNip,
        String verifiedByNama,
        Instant verifiedAt,
        LaporanPenandatanganResponse penandatangan,
        Object data
) {
}
