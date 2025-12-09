package cc.kertaskerja.bontang.rencanakinerja.domain.exception;

public class RencanaKinerjaNotFoundException extends RuntimeException {
    public RencanaKinerjaNotFoundException(Long id) {
        super("Rencana kinerja dengan id " + id + " tidak ditemukan.");
    }
}
