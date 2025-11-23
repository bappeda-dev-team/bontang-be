package cc.kertaskerja.bontang.kegiatan.domain.exception;

public class KegiatanAlreadyExistException extends RuntimeException {
    public KegiatanAlreadyExistException(String kodeKegiatan) {
        super("Kegiatan dengan kode " + kodeKegiatan + " sudah ada.");
    }
}
