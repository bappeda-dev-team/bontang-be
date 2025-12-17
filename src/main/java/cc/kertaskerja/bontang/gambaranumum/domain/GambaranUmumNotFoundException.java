package cc.kertaskerja.bontang.gambaranumum.domain;

public class GambaranUmumNotFoundException extends RuntimeException {
    public GambaranUmumNotFoundException(Long id) {
        super("Gambaran Umum dengan id " + id + " tidak ditemukan");
    }
}