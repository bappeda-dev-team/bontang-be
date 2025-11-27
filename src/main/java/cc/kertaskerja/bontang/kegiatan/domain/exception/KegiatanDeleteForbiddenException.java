package cc.kertaskerja.bontang.kegiatan.domain.exception;

/**
 * Dilempar ketika kegiatan masih direferensikan oleh entitas subkegiatan sehingga tidak bisa dihapus.
 */
public class KegiatanDeleteForbiddenException extends RuntimeException {
    public KegiatanDeleteForbiddenException(String kodeKegiatan) {
        super("Kegiatan dengan kode " + kodeKegiatan + " tidak dapat dihapus karena masih memiliki data subkegiatan terkait.");
    }
}
