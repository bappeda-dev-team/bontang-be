package cc.kertaskerja.bontang.jabatan.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record JabatanControllerRequest(

        @NotNull(message = "Nama jabatan tidak boleh kosong")
        @NotEmpty(message = "Nama jabatan tidak boleh kosong")
        String namaJabatan,

        @NotNull(message = "Kode jabatan tidak boleh kosong")
        @NotEmpty(message = "Kode jabatan tidak boleh kosong")
        String kodeJabatan,

        @NotNull(message = "Jenis jabatan tidak boleh kosong")
        @NotEmpty(message = "Jenis jabatan tidak boleh kosong")
        String jenisJabatan,

        @NotNull(message = "Kode OPD tidak boleh kosong")
        @NotEmpty(message = "Kode OPD tidak boleh kosong")
        String kodeOpd
) {
}
