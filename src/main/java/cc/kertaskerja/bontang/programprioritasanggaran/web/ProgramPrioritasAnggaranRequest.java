package cc.kertaskerja.bontang.programprioritasanggaran.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProgramPrioritasAnggaranRequest(
        @NotNull(message = "Id program prioritas tidak boleh kosong")
        Long idProgramPrioritas,

        @NotNull(message = "Kode OPD tidak boleh kosong")
        @NotEmpty(message = "Kode OPD tidak boleh kosong")
        String kodeOpd
) {
}
