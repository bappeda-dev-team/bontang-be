package cc.kertaskerja.bontang.opd.domain.exception;

/**
 * Dilempar ketika OPD masih direferensikan oleh entitas lain (program/kegiatan) sehingga tidak bisa dihapus.
 */
public class OpdDeleteForbiddenException extends RuntimeException {
    public OpdDeleteForbiddenException(String kodeOpd) {
        super("Opd dengan kode opd " + kodeOpd + " tidak dapat dihapus karena masih memiliki data terkait.");
    }
}
