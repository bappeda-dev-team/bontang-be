package cc.kertaskerja.bontang.subkegiatanopd.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SubKegiatanOpdRequest(
        @NotNull(message = "Kode SubKegiatan Opd tidak boleh kosong")
        @NotEmpty(message = "Kode SubKegiatan Opd tidak boleh kosong")
        String kodeSubKegiatanOpd,

        @NotNull(message = "Nama SubKegiatan tidak boleh kosong")
        @NotEmpty(message = "Nama SubKegiatan tidak boleh kosong")
        String namaSubKegiatanOpd,

        String kodeOpd,

        Integer tahun
) {
}
