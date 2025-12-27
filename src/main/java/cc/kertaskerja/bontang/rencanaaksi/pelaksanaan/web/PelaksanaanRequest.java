package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;

public record PelaksanaanRequest(
        @Nullable
        Integer idRencanaAksi,

        @NotNull(message = "Bulan tidak boleh kosong")
        Integer bulan,

        @NotNull(message = "Bobot tidak boleh kosong")
        Integer bobot
) {
}
