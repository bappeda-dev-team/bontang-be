package cc.kertaskerja.bontang.programprioritasanggaran.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProgramPrioritasAnggaranRequest(
        @NotNull(message = "Id program prioritas tidak boleh kosong")
        Long idProgramPrioritas,

        @NotNull(message = "Kode OPD tidak boleh kosong")
        @NotEmpty(message = "Kode OPD tidak boleh kosong")
        String kodeOpd,

        @Nullable
        List<Long> idRencanaKinerjaList
) {
}
