package cc.kertaskerja.bontang.koderekening.domain.exception;

public class KodeRekeningAlreadyExistException extends RuntimeException {
    public KodeRekeningAlreadyExistException(String kodeRekening) {
        super("Kode rekening dengan " + kodeRekening + " sudah ada.");
    }
}
