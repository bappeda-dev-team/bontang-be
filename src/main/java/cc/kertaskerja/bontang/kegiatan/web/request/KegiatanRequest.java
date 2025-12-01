package cc.kertaskerja.bontang.kegiatan.web.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record KegiatanRequest(
        @Nullable
        Long idKegiatan,

        @NotNull(message = "Kode kegiatan tidak boleh kosong")
        @NotEmpty(message = "Kode kegiatan tidak boleh kosong")
        String kodeKegiatan,

        @NotNull(message = "Nama kegiatan tidak boleh kosong")
        @NotEmpty(message = "Nama kegiatan tidak boleh kosong")
        String namaKegiatan,

        @NotNull(message = "Kode program tidak boleh kosong")
        @NotEmpty(message = "Kode program tidak boleh kosong")
        String kodeProgram
) {
}
