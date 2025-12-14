package cc.kertaskerja.bontang.subkegiatan.web.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SubKegiatanRequest(
        @NotNull(message = "Kode SubKegiatan tidak boleh kosong")
        @NotEmpty(message = "Kode SubKegiatan tidak boleh kosong")
        String kodeSubKegiatan,

        @NotNull(message = "Nama SubKegiatan tidak boleh kosong")
        @NotEmpty(message = "Nama SubKegiatan tidak boleh kosong")
        String namaSubKegiatan
) {
}
