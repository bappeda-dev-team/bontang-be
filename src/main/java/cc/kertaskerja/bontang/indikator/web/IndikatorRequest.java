package cc.kertaskerja.bontang.indikator.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record IndikatorRequest(
		@Nullable
        Long indikatorId,

        @NotNull(message = "Nama indikator tidak boleh kosong")
        @NotEmpty(message = "Nama indikator tidak boleh kosong")
        String namaIndikator
) {
}