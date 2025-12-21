package cc.kertaskerja.bontang.target.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record TargetRequest(
        @Nullable
        Long targetId,

        @NotNull(message = "Target tidak boleh kosong")
        @NotEmpty(message = "Target tidak boleh kosong")
        String target,

        @NotNull(message = "Satuan tidak boleh kosong")
        @NotEmpty(message = "Satuan tidak boleh kosong")
        String satuan,

        @NotNull(message = "Indikator ID tidak boleh kosong")
        Long indikatorId
) {
}
