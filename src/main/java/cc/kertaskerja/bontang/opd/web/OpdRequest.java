package cc.kertaskerja.bontang.opd.web;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OpdRequest(
        @Nullable
        Long opdId,

        @NotNull(message = "Kode Opd tidak boleh kosong")
        @NotEmpty(message = "Kode Opd tidak boleh kosong")
        String kodeOpd,

        @NotNull(message = "Nama Opd tidak boleh kosong")
        @NotEmpty(message = "Nama Opd tidak boleh kosong")
        String namaOpd,

        @Nullable
        @Valid
        List<OpdBidangUrusanRequest> bidangUrusan
) {
}
