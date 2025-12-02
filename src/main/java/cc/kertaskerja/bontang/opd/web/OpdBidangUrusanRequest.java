package cc.kertaskerja.bontang.opd.web;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OpdBidangUrusanRequest(
        @Nullable
        Long id,

        @NotNull(message = "Kode bidang urusan tidak boleh kosong")
        @NotEmpty(message = "Kode bidang urusan tidak boleh kosong")
        String kodeBidangUrusan,

        @NotNull(message = "Nama bidang urusan tidak boleh kosong")
        @NotEmpty(message = "Nama bidang urusan tidak boleh kosong")
        String namaBidangUrusan
) {
}
