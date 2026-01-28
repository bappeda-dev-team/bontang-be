package cc.kertaskerja.bontang.laporanrincianbelanja.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LaporanRincianBelanjaRencanaAksiResponse(
        @JsonProperty("renaksi_id")
        Long renaksiId,

        @JsonProperty("renaksi")
        String renaksi,

        Integer anggaran
) {
}

