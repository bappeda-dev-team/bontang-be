package cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain;

public class SubKegiatanRencanaKinerjaNotFoundException extends RuntimeException {
    public SubKegiatanRencanaKinerjaNotFoundException(Long id) {
        super("Sub kegiatan rencana kinerja dengan id " + id + " tidak ditemukan");
    }
}
