package cc.kertaskerja.bontang.subkegiatanrencanakinerja.web;

import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;

public record SubKegiatanRencanaKinerjaResponse(
        Long id,
        Integer rencana_kinerja_id,
        String kodeSubKegiatan,
        String namaSubKegiatan
) {
    public static SubKegiatanRencanaKinerjaResponse from(SubKegiatanRencanaKinerja data) {
        return new SubKegiatanRencanaKinerjaResponse(
                data.id(),
                data.idRekin(),
                data.kodeSubKegiatan(),
                data.namaSubKegiatan()
        );
    }
}
