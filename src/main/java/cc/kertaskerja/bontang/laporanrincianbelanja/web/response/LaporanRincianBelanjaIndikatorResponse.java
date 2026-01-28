package cc.kertaskerja.bontang.laporanrincianbelanja.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LaporanRincianBelanjaIndikatorResponse(
        @JsonProperty("id_indikator")
        Long idIndikator,

        @JsonProperty("nama_indikator")
        String namaIndikator,

        @JsonProperty("targets")
        List<LaporanRincianBelanjaTargetResponse> targets
) {
}

