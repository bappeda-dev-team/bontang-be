package cc.kertaskerja.bontang.sumberdana.domain.exception;

public class SumberDanaAlreadyExistException extends RuntimeException {
    public SumberDanaAlreadyExistException(Long id) {
        super("Sumber dana dengan id " + id + " sudah ada.");
    }
}
