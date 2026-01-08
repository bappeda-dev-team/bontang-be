package cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain.exception;

public class SubKegiatanRincianBelanjaNotFoundException extends RuntimeException {
    public SubKegiatanRincianBelanjaNotFoundException(String kodeSubKegiatan) {
        super("Sub kegiatan rincian belanja dengan kode sub kegiatan " + kodeSubKegiatan + " tidak ditemukan");
    }
}
