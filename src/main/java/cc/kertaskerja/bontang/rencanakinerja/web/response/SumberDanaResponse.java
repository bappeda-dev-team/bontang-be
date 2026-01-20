package cc.kertaskerja.bontang.rencanakinerja.web.response;

import java.util.Map;
import cc.kertaskerja.bontang.sumberdana.domain.SumberDana;

public record SumberDanaResponse(
    Long id,
    String kodeDanaLama,
    String sumberDana,
    String kodeDanaBaru
) {
    public static SumberDanaResponse from(SumberDana sumberDana) {
        return new SumberDanaResponse(
            sumberDana.id(),
            sumberDana.kodeDanaLama(),
            sumberDana.sumberDana(),
            sumberDana.kodeDanaBaru()
        );
    }

    public Map<String, Object> toMap() {
        return Map.of(
            "id", id,
            "kode_dana_lama", kodeDanaLama,
            "sumber_dana", sumberDana,
            "kode_dana_baru", kodeDanaBaru
        );
    }
}
