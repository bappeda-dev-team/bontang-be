package cc.kertaskerja.bontang.programprioritasopd.domain.exception;

public class ProgramPrioritasOpdNotFoundException extends RuntimeException {
    public ProgramPrioritasOpdNotFoundException(Long id) {
        super("Program prioritas OPD dengan id " + id + " tidak ditemukan.");
    }
}
