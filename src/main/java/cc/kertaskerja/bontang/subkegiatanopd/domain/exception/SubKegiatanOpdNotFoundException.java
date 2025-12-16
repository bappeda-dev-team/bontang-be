package cc.kertaskerja.bontang.subkegiatanopd.domain.exception;

public class SubKegiatanOpdNotFoundException extends RuntimeException {
    public SubKegiatanOpdNotFoundException(String kodeKegiatan) {
        super("Sub Kegiatan opd dengan kode " + kodeKegiatan + " tidak ditemukan.");
    }
}
