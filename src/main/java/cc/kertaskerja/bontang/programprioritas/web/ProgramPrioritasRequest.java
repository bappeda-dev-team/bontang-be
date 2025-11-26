package cc.kertaskerja.bontang.programprioritas.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

public record ProgramPrioritasRequest(
        @Id
        Long idProgramPrioritas,

        @NotNull(message = "Program prioritas tidak ditemukan")
        @NotEmpty(message = "Program prioritas tidak ditemukan")
        String programPrioritas
) {
}
