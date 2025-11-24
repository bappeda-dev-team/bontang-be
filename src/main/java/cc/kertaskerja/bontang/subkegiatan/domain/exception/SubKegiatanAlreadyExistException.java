package cc.kertaskerja.bontang.subkegiatan.domain.exception;

public class SubKegiatanAlreadyExistException extends RuntimeException {
    public SubKegiatanAlreadyExistException(String kodeSubKegiatan) {
        super("Sub kegiatan dengan " + kodeSubKegiatan + " sudah ada.");
    }
}
