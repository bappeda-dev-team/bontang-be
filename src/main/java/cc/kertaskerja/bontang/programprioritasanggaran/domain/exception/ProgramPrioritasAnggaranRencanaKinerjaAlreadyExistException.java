package cc.kertaskerja.bontang.programprioritasanggaran.domain.exception;

public class ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException extends RuntimeException {
    public ProgramPrioritasAnggaranRencanaKinerjaAlreadyExistException(Long idRencanaKinerja, Long idProgramPrioritasAnggaran) {
        super("Rencana kinerja dengan id " + idRencanaKinerja + " sudah ada dalam program prioritas anggaran dengan id " + idProgramPrioritasAnggaran + ".");
    }
}
