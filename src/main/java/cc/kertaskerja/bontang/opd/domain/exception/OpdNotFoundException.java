package cc.kertaskerja.bontang.opd.domain.exception;

public class OpdNotFoundException extends RuntimeException {
    public OpdNotFoundException(String kodeOpd) {
        super("Opd dengan kode opd " + kodeOpd + " tidak ditemukan.");
    }
}
