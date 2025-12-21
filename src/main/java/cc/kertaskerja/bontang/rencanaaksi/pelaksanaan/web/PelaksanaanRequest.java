package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.web;

import jakarta.validation.constraints.NotNull;

public record PelaksanaanRequest(
        @NotNull(message = "Id rencana aksi tidak boleh kosong")
        Integer idRencanaAksi,

        @NotNull(message = "Bulan tidak boleh kosong")
        Integer bulan,

        @NotNull(message = "Bobot tidak boleh kosong")
        Integer bobot
) {
}
