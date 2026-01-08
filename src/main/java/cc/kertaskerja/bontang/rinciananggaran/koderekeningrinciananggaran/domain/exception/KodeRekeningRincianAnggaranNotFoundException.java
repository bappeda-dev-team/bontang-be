package cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain.exception;

public class KodeRekeningRincianAnggaranNotFoundException extends RuntimeException {
    public KodeRekeningRincianAnggaranNotFoundException(Long id) {
        super("Kode rekening rincian anggaran dengan id " + id + " tidak ditemukan");
    }
}
