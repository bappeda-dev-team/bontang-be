package cc.kertaskerja.bontang.subkegiatan.web;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SubKegiatanRequest(
        @Nullable
        Long idSubKegiatan,

        @NotNull(message = "Kode SubKegiatan tidak boleh kosong")
        @NotEmpty(message = "Kode SubKegiatan tidak boleh kosong")
        String kodeSubKegiatan,

        @NotNull(message = "Nama SubKegiatan tidak boleh kosong")
        @NotEmpty(message = "Nama SubKegiatan tidak boleh kosong")
        String namaSubKegiatan
) {
}
