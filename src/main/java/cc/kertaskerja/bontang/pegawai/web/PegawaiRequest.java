package cc.kertaskerja.bontang.pegawai.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PegawaiRequest(
        @Nullable
        String kodeOpd,

        @Nullable
        Integer tahun,

        @NotNull(message = "Nama pegawai tidak boleh kosong")
        @NotEmpty(message = "Nama pegawai tidak boleh kosong")
        String namaPegawai,

        @NotNull(message = "NIP tidak boleh kosong")
        @NotEmpty(message = "NIP tidak boleh kosong")
        String nip,

        @NotNull(message = "Email tidak boleh kosong")
        @NotEmpty(message = "Email tidak boleh kosong")
        String email,

        @NotNull(message = "Jabatan dinas tidak boleh kosong")
        @NotEmpty(message = "Jabatan dinas tidak boleh kosong")
        String jabatanDinas,

        @NotNull(message = "Jabatan tim tidak boleh kosong")
        @NotEmpty(message = "Jabatan tim tidak boleh kosong")
        String jabatanTim
) {
}
