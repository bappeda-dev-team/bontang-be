package cc.kertaskerja.bontang.laporanrincianbelanja.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LaporanRincianBelanjaSubkegiatanResponse(
        @JsonProperty("kode_subkegiatan")
        String kodeSubkegiatan,

        @JsonProperty("nama_subkegiatan")
        String namaSubkegiatan,

        @JsonProperty("indikator_subkegiatan")
        String indikatorSubkegiatan,

        @JsonProperty("total_anggaran")
        Integer totalAnggaran,

        @JsonProperty("rincian_belanja")
        List<LaporanRincianBelanjaRencanaKinerjaResponse> rincianBelanja
) {
}

