package cc.kertaskerja.bontang.laporanrincianbelanja.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LaporanRincianBelanjaRencanaKinerjaResponse(
        @JsonProperty("rencana_kinerja_id")
        Long rencanaKinerjaId,

        @JsonProperty("rencana_kinerja")
        String rencanaKinerja,

        @JsonProperty("id_pegawai")
        String idPegawai,

        @JsonProperty("nama_pegawai")
        String namaPegawai,

        @JsonProperty("indikator")
        List<LaporanRincianBelanjaIndikatorResponse> indikator,

        @JsonProperty("total_anggaran")
        Integer totalAnggaran,

        @JsonProperty("rencana_aksi")
        List<LaporanRincianBelanjaRencanaAksiResponse> rencanaAksi
) {
}

