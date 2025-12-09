package cc.kertaskerja.bontang.rencanakinerja.domain.exception;

public class RencanaKinerjaAlreadyExist extends RuntimeException {
    public RencanaKinerjaAlreadyExist(Long id) {
        super("Rencana kinerja dengan id " + id + " sudah ada.");
    }
}
