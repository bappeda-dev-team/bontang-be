package cc.kertaskerja.bontang.kegiatan.domain.exception;

public class KegiatanNotFoundException extends RuntimeException {
    public KegiatanNotFoundException(String kodeKegiatan) {
        super("Kegiatan dengan kode " + kodeKegiatan + " tidak ditemukan.");
    }
}
