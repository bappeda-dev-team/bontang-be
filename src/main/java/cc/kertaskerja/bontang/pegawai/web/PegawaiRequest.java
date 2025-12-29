package cc.kertaskerja.bontang.pegawai.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PegawaiRequest(

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
