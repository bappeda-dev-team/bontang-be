package cc.kertaskerja.bontang.laporanverifikasi.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LaporanVerifikasiRequest(
        @NotBlank String jenisLaporan,
        @NotBlank String kodeOpd,
        @NotNull Integer tahun,
        String filterHash,
        @NotBlank String tahapVerifikasi
) {
}
