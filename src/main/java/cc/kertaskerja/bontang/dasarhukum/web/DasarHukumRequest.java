package cc.kertaskerja.bontang.dasarhukum.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DasarHukumRequest(
		@Nullable
        Long dasarHukumId,

        @NotNull(message = "Peraturan terkait tidak boleh kosong")
        @NotEmpty(message = "Peraturan terkait tidak boleh kosong")
        String peraturanTerkait,

        @NotNull(message = "Uraian tidak boleh kosong")
        @NotEmpty(message = "Uraian tidak boleh kosong")
        String uraian,

        @Nullable
        String kodeOpd,

        @Nullable
        Integer tahun
) {
}