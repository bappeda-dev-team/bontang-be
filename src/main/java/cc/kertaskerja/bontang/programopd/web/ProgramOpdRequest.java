package cc.kertaskerja.bontang.programopd.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProgramOpdRequest(
        @NotNull(message = "Kode Program Opd tidak boleh kosong")
        @NotEmpty(message = "Kode Program Opd tidak boleh kosong")
        String kodeProgramOpd,

        @NotNull(message = "Nama Program tidak boleh kosong")
        @NotEmpty(message = "Nama Program tidak boleh kosong")
        String namaProgramOpd,

        String kodeOpd,

        Integer tahun
) {
}
