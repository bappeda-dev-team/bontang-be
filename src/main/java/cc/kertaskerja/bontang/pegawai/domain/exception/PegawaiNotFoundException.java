package cc.kertaskerja.bontang.pegawai.domain.exception;

public class PegawaiNotFoundException extends RuntimeException {
    public PegawaiNotFoundException(String nip) {
        super("Pegawai dengan nip " + nip + " tidak ditemukan.");
    }
}
