package cc.kertaskerja.bontang.kegiatanopd.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record KegiatanOpdRequest(
        @NotNull(message = "Kode Kegiatan Opd tidak boleh kosong")
        @NotEmpty(message = "Kode Kegiatan Opd tidak boleh kosong")
        String kodeKegiatanOpd,

        @NotNull(message = "Nama Kegiatan tidak boleh kosong")
        @NotEmpty(message = "Nama Kegiatan tidak boleh kosong")
        String namaKegiatanOpd,

        String kodeOpd,

        Integer tahun
) {
}
