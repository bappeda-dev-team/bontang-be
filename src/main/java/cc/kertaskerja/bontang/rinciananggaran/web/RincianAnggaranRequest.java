package cc.kertaskerja.bontang.rinciananggaran.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RincianAnggaranRequest(
        @NotNull(message = "Nama rincian anggaran tidak boleh kosong")
        @NotEmpty(message = "Nama rincian anggaran tidak boleh kosong")
        String namaRincianAnggaran,

        @NotNull(message = "Urutan tidak boleh kosong")
        Integer urutan
) {
}
