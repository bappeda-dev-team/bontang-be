package cc.kertaskerja.bontang.subkegiatanrencanakinerja.web;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SubKegiatanRencanaKinerjaRequest(
        @Nullable
        Long subKegiatanRencanaKinerjaId,

        @NotNull(message = "Id rencana kinerja tidak boleh kosong")
        Integer idRencanaKinerja,

        @NotNull(message = "Kode sub kegiatan rencana kinerja tidak boleh kosong")
        @NotEmpty(message = "Kode sub kegiatan rencana kinerja tidak boleh kosong")
        String kodeSubKegiatanRencanaKinerja,

       @NotNull(message = "Nama sub kegiatan rencana kinerja tidak boleh kosong")
        @NotEmpty(message = "Nama sub kegiatan rencana kinerja tidak boleh kosong")
        String namaSubKegiatanRencanaKinerja
) {
}
