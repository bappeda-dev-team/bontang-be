package cc.kertaskerja.bontang.programopd.domain.exception;

public class ProgramOpdNotFoundException extends RuntimeException {
    public ProgramOpdNotFoundException(String kodeProgram) {
        super("Program opd dengan kode " + kodeProgram + " tidak ditemukan.");
    }
}
