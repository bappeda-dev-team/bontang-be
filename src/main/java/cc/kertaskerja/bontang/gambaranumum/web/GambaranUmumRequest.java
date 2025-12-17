package cc.kertaskerja.bontang.gambaranumum.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record GambaranUmumRequest(
		@Nullable
        Long gambaranUmumId,

        @NotNull(message = "Gambaran umum tidak boleh kosong")
        @NotEmpty(message = "Gambaran umum tidak boleh kosong")
        String gambaranUmum,

        @NotNull(message = "Uraian tidak boleh kosong")
        @NotEmpty(message = "Uraian tidak boleh kosong")
        String uraian,

        @Nullable
        String kodeOpd,

        @Nullable
        Integer tahun
) {
}