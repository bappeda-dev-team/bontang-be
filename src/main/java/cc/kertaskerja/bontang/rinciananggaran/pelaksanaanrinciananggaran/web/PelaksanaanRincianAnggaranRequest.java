package cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;

public record PelaksanaanRincianAnggaranRequest(
        @Nullable
        Integer idRincianAnggaran,

        @NotNull(message = "Bulan tidak boleh kosong")
        Integer bulan,

        @NotNull(message = "Bobot tidak boleh kosong")
        Integer bobot
) {
}
