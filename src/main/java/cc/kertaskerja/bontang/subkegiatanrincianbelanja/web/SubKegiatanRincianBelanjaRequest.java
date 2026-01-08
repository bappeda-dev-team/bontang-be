package cc.kertaskerja.bontang.subkegiatanrincianbelanja.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SubKegiatanRincianBelanjaRequest(
        @NotNull(message = "Kode sub kegiatan rincian kinerja tidak boleh kosong")
        @NotEmpty(message = "Kode sub kegiatan rincian kinerja tidak boleh kosong")
        String kodeSubKegiatanRincianKinerja,

        @NotNull(message = "Nama sub kegiatan rincian kinerja tidak boleh kosong")
        @NotEmpty(message = "Nama sub kegiatan rincian kinerja tidak boleh kosong")
        String namaSubKegiatanRincianKinerja,

        @Nullable
        Integer pagu
) {
}
