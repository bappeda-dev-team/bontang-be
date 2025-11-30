package cc.kertaskerja.bontang.program.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProgramBatchRequest(
        @NotNull(message = "Kode Program tidak boleh kosong")
        @NotEmpty(message = "Kode Program tidak boleh kosong")
        List<String> kodeProgram
) {
}
