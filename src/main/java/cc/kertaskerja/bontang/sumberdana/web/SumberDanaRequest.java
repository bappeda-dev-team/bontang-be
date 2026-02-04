package cc.kertaskerja.bontang.sumberdana.web;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SumberDanaRequest(
        @Nullable
        Long idSumberDana,

        @NotNull(message = "Kode dana lama tidak boleh kosong")
        @NotEmpty(message = "Kode dana lama tidak boleh kosong")
        String kodeDanaLama,

        @NotNull(message = "Sumber dana tidak boleh kosong")
        @NotEmpty(message = "Sumber dana tidak boleh kosong")
        String sumberDana,

        @NotNull(message = "Kode dana baru tidak boleh kosong")
        @NotEmpty(message = "Kode dana baru tidak boleh kosong")
        String kodeDanaBaru,

        @Nullable
        String setInput
) {
}
