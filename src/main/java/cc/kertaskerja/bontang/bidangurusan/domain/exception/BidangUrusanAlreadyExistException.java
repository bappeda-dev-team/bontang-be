package cc.kertaskerja.bontang.bidangurusan.domain.exception;

public class BidangUrusanAlreadyExistException extends RuntimeException {
    public BidangUrusanAlreadyExistException(String kodeBidangUrusan, String kodeOpd) {
        super("Bidang urusan dengan kode " + kodeBidangUrusan + " sudah terdaftar untuk OPD dengan kode opd : " + kodeOpd + ".");
    }
}
