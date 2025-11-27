package cc.kertaskerja.bontang.bidangurusan.domain.exception;

/**
 * Dilempar ketika bidang urusan masih direferensikan oleh entitas program sehingga tidak bisa dihapus.
 */
public class BidangUrusanDeleteForbiddenException extends RuntimeException {
    public BidangUrusanDeleteForbiddenException(String kodeBidangUrusan) {
        super("Bidang urusan dengan kode " + kodeBidangUrusan + " tidak dapat dihapus karena masih memiliki data program terkait.");
    }
}
