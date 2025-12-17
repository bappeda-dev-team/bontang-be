package cc.kertaskerja.bontang.dasarhukum.domain;

public class DasarHukumNotFoundException extends RuntimeException {
	public DasarHukumNotFoundException(Long id) {
        super("Dasar Hukum dengan id " + id + " tidak ditemukan.");
    }
}