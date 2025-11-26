package cc.kertaskerja.bontang.koderekening.web;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record KodeRekeningRequest(
        @Nullable
        Long idKodeRekening,

        @NotNull(message = "Kode Rekening tidak boleh kosong")
        @NotEmpty(message = "Kode Rekening tidak boleh kosong")
        String kodeRekening,

        @NotNull(message = "Nama Rekening tidak boleh kosong")
        @NotEmpty(message = "Nama Rekening tidak boleh kosong")
        String namaRekening
) {
}
