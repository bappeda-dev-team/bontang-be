package cc.kertaskerja.bontang.programprioritasanggaran.domain.exception;

public class ProgramPrioritasAnggaranAlreadyExistException extends RuntimeException {
    public ProgramPrioritasAnggaranAlreadyExistException(Long id) {
        super("Program prioritas anggaran dengan id " + id + " sudah ada.");
    }
}
