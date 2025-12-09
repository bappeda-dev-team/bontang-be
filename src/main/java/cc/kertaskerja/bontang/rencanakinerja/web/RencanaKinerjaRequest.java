package cc.kertaskerja.bontang.rencanakinerja.web;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RencanaKinerjaRequest(
    @Nullable
    Long idSumberDana,

    @NotNull(message = "Rencana kinerja lama tidak boleh kosong")
    @NotEmpty(message = "Rencana kinerja lama tidak boleh kosong")
    String rencanaKinerja,

    @NotNull(message = "Indikator tidak boleh kosong")
    @NotEmpty(message = "Indikator tidak boleh kosong")
    String indikator,

    @NotNull(message = "Target baru tidak boleh kosong")
    @NotEmpty(message = "Target baru tidak boleh kosong")
    String target,

    @NotNull(message = "Sumber dana baru tidak boleh kosong")
    @NotEmpty(message = "Sumber dana baru tidak boleh kosong")
    String sumberDana,

    @NotNull(message = "Keterangan baru tidak boleh kosong")
    @NotEmpty(message = "Keterangan baru tidak boleh kosong")
    String keterangan
) {
}
