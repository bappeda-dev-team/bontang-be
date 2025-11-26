package cc.kertaskerja.bontang.programprioritas.domain.exception;

public class ProgramPrioritasNotFoundException extends RuntimeException {
    public ProgramPrioritasNotFoundException(Long id) {
        super("Program prioritas dengan id " + " tidak ditemukan.");
    }
}
