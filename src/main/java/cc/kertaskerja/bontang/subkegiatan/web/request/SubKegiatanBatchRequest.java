package cc.kertaskerja.bontang.subkegiatan.web.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SubKegiatanBatchRequest(
        @NotNull(message = "Kode SubKegiatan tidak boleh kosong")
        @NotEmpty(message = "Kode SubKegiatan tidak boleh kosong")
        List<String> kodeSubKegiatan
) {
}
