package cc.kertaskerja.bontang.bidangurusan.domain.exception;

public class BidangUrusanNotFoundException extends RuntimeException {
    public BidangUrusanNotFoundException(String identifier) {
        super("Bidang urusan  dengan nama " + identifier + " tidak ditemukan di Tower Data.");
    }
}
