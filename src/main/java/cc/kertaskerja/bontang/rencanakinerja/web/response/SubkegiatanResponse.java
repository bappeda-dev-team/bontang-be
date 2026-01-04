package cc.kertaskerja.bontang.rencanakinerja.web.response;

import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;

public record SubkegiatanResponse(
    Long id,
    Integer rencanaKinerjaId,
    String kodeSubkegiatan,
    String namaSubkegiatan
) {
    public static SubkegiatanResponse from(SubKegiatanRencanaKinerja data) {
        return new SubkegiatanResponse(
            data.id(),
            data.idRekin(),
            data.kodeSubKegiatan(),
            data.namaSubKegiatan()
        );
    }
}
