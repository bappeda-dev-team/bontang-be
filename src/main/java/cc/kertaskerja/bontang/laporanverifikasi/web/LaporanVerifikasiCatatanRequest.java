package cc.kertaskerja.bontang.laporanverifikasi.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LaporanVerifikasiCatatanRequest(
        @NotBlank String jenisLaporan,
        @NotBlank String kodeOpd,
        @NotNull Integer tahun,
        String filterHash,
        @NotBlank String tahapVerifikasi,
        @NotBlank @Size(max = 255) String catatan
) {
}
