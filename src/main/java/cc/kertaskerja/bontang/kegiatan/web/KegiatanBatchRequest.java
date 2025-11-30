package cc.kertaskerja.bontang.kegiatan.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record KegiatanBatchRequest(
        @NotNull(message = "Kode Kegiatan tidak boleh kosong")
        @NotEmpty(message = "Kode Kegiatan tidak boleh kosong")
        List<String> kodeKegiatan
) {
}
