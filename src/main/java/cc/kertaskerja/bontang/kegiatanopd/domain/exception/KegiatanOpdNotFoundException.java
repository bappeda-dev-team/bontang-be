package cc.kertaskerja.bontang.kegiatanopd.domain.exception;

public class KegiatanOpdNotFoundException extends RuntimeException {
    public KegiatanOpdNotFoundException(String kodeKegiatan) {
        super("Kegiatan opd dengan kode " + kodeKegiatan + " tidak ditemukan.");
    }
}
