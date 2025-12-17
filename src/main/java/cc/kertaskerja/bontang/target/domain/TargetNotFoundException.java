package cc.kertaskerja.bontang.target.domain;

public class TargetNotFoundException extends RuntimeException {
    public TargetNotFoundException(Long id) {
        super("Target dengan ID " + id + " tidak ditemukan");
    }
}