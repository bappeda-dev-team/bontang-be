package cc.kertaskerja.bontang.rencanaaksi.domain.exception;

public class RencanaAksiAlreadyExistException extends RuntimeException {
	public RencanaAksiAlreadyExistException(Long id) {
	    super("Rencana aksi dengan id " + id + " sudah ada.");
	}
}
