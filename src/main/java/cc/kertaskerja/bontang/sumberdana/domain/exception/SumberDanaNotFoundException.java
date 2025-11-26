package cc.kertaskerja.bontang.sumberdana.domain.exception;

public class SumberDanaNotFoundException extends RuntimeException {
    public SumberDanaNotFoundException(Long id) {
        super("Sumber dana dengan id " + id + " tidak ditemukan.");
    }
}
