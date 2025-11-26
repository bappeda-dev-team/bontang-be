package cc.kertaskerja.bontang.programprioritas.domain.exception;

public class ProgramPrioritasAlreadyExistException extends RuntimeException {
    public ProgramPrioritasAlreadyExistException(Long id) {
        super("Program prioritas dengan id " + id + " sudah ada.");
    }
}
