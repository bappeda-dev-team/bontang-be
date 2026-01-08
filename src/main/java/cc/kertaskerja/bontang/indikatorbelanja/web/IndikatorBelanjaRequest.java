package cc.kertaskerja.bontang.indikatorbelanja.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;

public record IndikatorBelanjaRequest(
        @Nullable
        Long indikatorBelanjaId,

        @Nullable
        String namaIndikatorBelanja,

        @NotNull(message = "Rincian belanja id tidak boleh kosong")
        Long rincianBelanjaId
) {
}
