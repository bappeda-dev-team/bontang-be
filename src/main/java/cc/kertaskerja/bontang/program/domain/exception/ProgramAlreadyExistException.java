package cc.kertaskerja.bontang.program.domain.exception;

public class ProgramAlreadyExistException extends RuntimeException {
    public ProgramAlreadyExistException(String kodeProgram) {
        super("Program dengan kode " + kodeProgram + " sudah ada.");
    }
}
