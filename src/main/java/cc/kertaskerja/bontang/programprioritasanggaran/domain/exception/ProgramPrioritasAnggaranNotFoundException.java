package cc.kertaskerja.bontang.programprioritasanggaran.domain.exception;

public class ProgramPrioritasAnggaranNotFoundException extends RuntimeException {
    public ProgramPrioritasAnggaranNotFoundException(Long id) {
        super("Program prioritas anggaran dengan id " + id + " tidak ditemukan.");
    }
}
