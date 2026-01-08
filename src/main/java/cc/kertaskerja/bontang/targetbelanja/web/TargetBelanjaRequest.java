package cc.kertaskerja.bontang.targetbelanja.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;

public record TargetBelanjaRequest(
        @Nullable
        Long targetBelanjaId,

        @Nullable
        String namaTargetBelanja,

        @Nullable
        String satuanTargetBelanja,

        @NotNull(message = "Indikator belanja ID tidak boleh kosong")
        Long indikatorBelanjaId
) {
}
