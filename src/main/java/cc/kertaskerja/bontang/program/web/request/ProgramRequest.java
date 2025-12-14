package cc.kertaskerja.bontang.program.web.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProgramRequest(
        @NotNull(message = "Kode Program tidak boleh kosong")
        @NotEmpty(message = "Kode Program tidak boleh kosong")
        String kodeProgram,

        @NotNull(message = "Nama Program tidak boleh kosong")
        @NotEmpty(message = "Nama Program tidak boleh kosong")
        String namaProgram
) {
}
