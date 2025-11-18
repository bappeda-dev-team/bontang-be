package cc.kertaskerja.bontang.opd.domain.exception;

public class OpdAlreadyExistException extends RuntimeException {
    public OpdAlreadyExistException() {
        super("Opd sudah ada.");
    }

    public OpdAlreadyExistException(String kodeOpd) {
        super("Opd dengan kode opd " + kodeOpd + " sudah ada.");
    }
}
