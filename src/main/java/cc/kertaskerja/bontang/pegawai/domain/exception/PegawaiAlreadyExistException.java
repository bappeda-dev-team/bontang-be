package cc.kertaskerja.bontang.pegawai.domain.exception;

public class PegawaiAlreadyExistException extends RuntimeException {
    public PegawaiAlreadyExistException(String nip) {
        super("Pegawai dengan nip " + nip + " sudah ada.");
    }
}
