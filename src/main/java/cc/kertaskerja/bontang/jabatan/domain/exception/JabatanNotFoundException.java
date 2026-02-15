package cc.kertaskerja.bontang.jabatan.domain.exception;

public class JabatanNotFoundException extends RuntimeException {
    public JabatanNotFoundException(String kodeJabatan) {
        super("Jabatan dengan kode " + kodeJabatan + " tidak ditemukan.");
    }
}
