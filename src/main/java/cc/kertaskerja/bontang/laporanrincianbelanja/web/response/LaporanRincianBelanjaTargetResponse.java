package cc.kertaskerja.bontang.laporanrincianbelanja.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LaporanRincianBelanjaTargetResponse(
        @JsonProperty("id_target")
        Long idTarget,
        String target,
        String satuan
) {
}

