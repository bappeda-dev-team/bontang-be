package cc.kertaskerja.bontang.subkegiatan.domain.exception;

public class SubKegiatanNotFoundException extends RuntimeException {
    public SubKegiatanNotFoundException(String kodeSubKegiatan) {
        super("Sub kegiatan dengan kode " + kodeSubKegiatan + " tidak ditemukan.");
    }
}
