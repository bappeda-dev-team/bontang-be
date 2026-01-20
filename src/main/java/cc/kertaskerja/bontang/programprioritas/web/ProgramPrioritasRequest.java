package cc.kertaskerja.bontang.programprioritas.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProgramPrioritasRequest(
        @NotNull(message = "Program prioritas tidak boleh kosong")
        @NotEmpty(message = "Program prioritas tidak boleh kosong")
        String programPrioritas,

        @NotNull(message = "tahun prioritas tidak boleh kosong")
        Integer tahun,

        @Nullable
        String keterangan,

        @Nullable
        Integer periodeTahunAwal,

        @Nullable
        Integer periodeTahunAkhir,

        @Nullable
        String status
) {
}
