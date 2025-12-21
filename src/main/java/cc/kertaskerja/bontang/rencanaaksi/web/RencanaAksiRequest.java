package cc.kertaskerja.bontang.rencanaaksi.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RencanaAksiRequest(
        @NotNull(message = "Id rencana kinerja tidak boleh kosong")
        Integer idRencanaKinerja,

        @NotNull(message = "Kode opd tidak boleh kosong")
        @NotEmpty(message = "Kode opd tidak boleh kosong")
        String kodeOpd,

        @NotNull(message = "Urutan tidak boleh kosong")
        Integer urutan,

        @NotNull(message = "Nama rencana aksi lama tidak boleh kosong")
        @NotEmpty(message = "Nama rencana aksi lama tidak boleh kosong")
        String namaRencanaAksi

) {
}
