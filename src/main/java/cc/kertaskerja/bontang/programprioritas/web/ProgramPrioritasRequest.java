package cc.kertaskerja.bontang.programprioritas.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import io.micrometer.common.lang.Nullable;

public record ProgramPrioritasRequest(
        @Nullable
        Integer idSubKegiatanOpd,

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
        String status,

        @Nullable
        String kodeOpd,

        @Nullable
        String kodeSubKegiatanOpd
) {
}
