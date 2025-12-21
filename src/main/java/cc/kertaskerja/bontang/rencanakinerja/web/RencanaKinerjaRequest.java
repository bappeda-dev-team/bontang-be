package cc.kertaskerja.bontang.rencanakinerja.web;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record RencanaKinerjaRequest(
    @Nullable
    Long idSumberDana,

    @NotNull(message = "Rencana kinerja lama tidak boleh kosong")
    @NotEmpty(message = "Rencana kinerja lama tidak boleh kosong")
    String rencanaKinerja,

    @Nullable
    String kodeOpd,

    @Nullable
    String nipPegawai,

    @Nullable
    String createdBy,

    @Nullable
    Integer tahun,

    @Nullable
    String statusRencanaKinerja,

    @Nullable
    String namaOpd,

    @Nullable
    String namaPegawai,

    @NotNull(message = "Sumber dana baru tidak boleh kosong")
    @NotEmpty(message = "Sumber dana baru tidak boleh kosong")
    String sumberDana,

    @NotNull(message = "Keterangan baru tidak boleh kosong")
    @NotEmpty(message = "Keterangan baru tidak boleh kosong")
    String keterangan,

    @Nullable
    List<IndikatorData> indikatorList
) {
    public record IndikatorData(
        @Nullable
        String namaIndikator,
        
        @Nullable
        List<TargetData> targetList
    ) {
    }
    
    public record TargetData(
        @Nullable
        String target,
        
        @Nullable
        String satuan
    ) {
    }
}
