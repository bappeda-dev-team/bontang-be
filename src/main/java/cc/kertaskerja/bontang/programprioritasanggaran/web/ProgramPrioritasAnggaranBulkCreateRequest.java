package cc.kertaskerja.bontang.programprioritasanggaran.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProgramPrioritasAnggaranBulkCreateRequest(
        @NotEmpty(message = "Daftar program prioritas tidak boleh kosong")
        @Valid
        List<ProgramPrioritasAnggaranRequest> programPrioritas
) {
}
