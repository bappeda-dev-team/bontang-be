package cc.kertaskerja.bontang.rincianbelanja.web;

import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RincianBelanjaRequest(
        @Nullable
        Long idSumberDana,

        @Nullable
        Long idRencanaKinerja,

        @Nullable
        Long idRencanaAksi,

        @Nullable
        String kodeOpd,

        @Nullable
        String namaOpd,

        @Nullable
        Integer tahun,

        @Nullable
        String statusRincianBelanja,

        @Nullable
        String nipPegawai,

        @Nullable
        String namaPegawai,

        @NotNull(message = "Sumber dana tidak boleh kosong")
        @NotEmpty(message = "Sumber dana tidak boleh kosong")
        String sumberDana,

        @NotNull(message = "Rencana kinerja tidak boleh kosong")
        @NotEmpty(message = "Rencana kinerja tidak boleh kosong")
        String rencanaKinerja,

        @NotNull(message = "Rencana aksi tidak boleh kosong")
        @NotEmpty(message = "Rencana aksi tidak boleh kosong")
        String rencanaAksi,

        @Nullable
        List<IndikatorData> indikatorList
) {
    public record IndikatorData(
        @Nullable
        Long idIndikator,

        @Nullable
        String namaIndikator,

        @Nullable
        List<TargetData> targetList
    ) {
    }

    public record TargetData(
        @Nullable
        Long idTarget,
        
        @Nullable
        String namaTarget,
        
        @Nullable
        String satuan
    ) {
    }
}
