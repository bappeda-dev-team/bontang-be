package cc.kertaskerja.bontang.program.domain.exception;

/**
 * Dilempar ketika program masih direferensikan oleh entitas kegiatan sehingga tidak bisa dihapus.
 */
public class ProgramDeleteForbiddenException extends RuntimeException {
    public ProgramDeleteForbiddenException(String kodeProgram) {
        super("Program dengan kode " + kodeProgram + " tidak dapat dihapus karena masih memiliki data kegiatan terkait.");
    }
}
