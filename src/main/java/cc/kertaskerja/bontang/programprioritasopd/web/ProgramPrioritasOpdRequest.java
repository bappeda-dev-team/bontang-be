package cc.kertaskerja.bontang.programprioritasopd.web;

import jakarta.validation.constraints.NotNull;

import io.micrometer.common.lang.Nullable;

public record ProgramPrioritasOpdRequest(
        @Nullable
        Long idProgramPrioritas,

        @Nullable
        Long idSubkegiatanOpd,

        @Nullable
        Long idRencanaKinerja,

        @NotNull(message = "Tahun tidak boleh kosong")
        Integer tahun,

        @Nullable
        String kodeOpd,

        @Nullable
        String status,

        @Nullable
        String keterangan
) {
}
