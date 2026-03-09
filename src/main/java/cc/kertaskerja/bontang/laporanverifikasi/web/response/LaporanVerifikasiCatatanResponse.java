package cc.kertaskerja.bontang.laporanverifikasi.web.response;

import java.time.Instant;

public record LaporanVerifikasiCatatanResponse(
        String jenisLaporan,
        String kodeOpd,
        Integer tahun,
        String filterHash,
        String tahapVerifikasi,
        String catatan,
        String catatanByNip,
        String catatanByNama,
        Instant catatanAt
) {
}
