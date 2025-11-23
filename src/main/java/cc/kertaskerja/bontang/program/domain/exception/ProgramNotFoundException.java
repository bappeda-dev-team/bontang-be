package cc.kertaskerja.bontang.program.domain.exception;

public class ProgramNotFoundException extends RuntimeException {
    public ProgramNotFoundException(String kodeProgram) {
        super("Program dengan kode " + kodeProgram + " tidak ditemukan.");
    }
}
