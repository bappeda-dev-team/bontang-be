package cc.kertaskerja.bontang.koderekening.domain.exception;

public class KodeRekeningNotFoundException extends RuntimeException {
    public KodeRekeningNotFoundException(String kodeRekening) {
        super("Kode rekening dengan kode " + kodeRekening + " tidak ditemukan.");
    }
}
