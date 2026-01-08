package cc.kertaskerja.bontang.targetbelanja.domain.exception;

public class TargetBelanjaNotFoundException extends RuntimeException {
    public TargetBelanjaNotFoundException(Long id) {
        super("Target belanja dengan ID " + id + " tidak ditemukan");
    }
}
