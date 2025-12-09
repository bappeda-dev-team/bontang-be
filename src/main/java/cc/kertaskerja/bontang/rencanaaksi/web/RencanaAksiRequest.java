package cc.kertaskerja.bontang.rencanaaksi.web;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RencanaAksiRequest(
    @Nullable
    Long idSumberDana,

    @NotNull(message = "Rencana aksi lama tidak boleh kosong")
    @NotEmpty(message = "Rencana aksi lama tidak boleh kosong")
    String rencanaAksi,

    @NotNull(message = "Urutan tidak boleh kosong")
    Integer urutan
) {
}
