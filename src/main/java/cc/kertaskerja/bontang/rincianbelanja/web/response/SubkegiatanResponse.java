package cc.kertaskerja.bontang.rincianbelanja.web.response;

import cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain.SubKegiatanRincianBelanja;

public record SubkegiatanResponse(
    Long id,
    Integer rincianBelanjaId,
    String kodeSubKegiatan,
    String namaSubKegiatan
) {
    public static SubkegiatanResponse from(SubKegiatanRincianBelanja data) {
        return new SubkegiatanResponse(
            data.id(),
            data.idRincianBelanja(),
            data.kodeSubKegiatanRincianBelanja(),
            data.namaSubKegiatanRincianBelanja()
        );
    }
}
