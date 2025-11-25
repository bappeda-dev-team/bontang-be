package cc.kertaskerja.bontang.bidangurusan.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BidangUrusanRequest(
        @NotNull(message = "Nama bidang urusan tidak boleh kosong")
        @NotEmpty(message = "Nama bidang urusan tidak boleh kosong")
        String namaBidangUrusan
) {
}
