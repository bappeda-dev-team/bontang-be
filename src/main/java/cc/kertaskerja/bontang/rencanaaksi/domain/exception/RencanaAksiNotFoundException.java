package cc.kertaskerja.bontang.rencanaaksi.domain.exception;

public class RencanaAksiNotFoundException extends RuntimeException {
	public RencanaAksiNotFoundException(Long id) {
	    super("Rencana aksi dengan " + id + " tidak ditemukan.");
	}
}
