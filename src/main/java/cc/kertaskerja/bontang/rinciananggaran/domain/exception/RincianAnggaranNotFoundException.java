package cc.kertaskerja.bontang.rinciananggaran.domain.exception;

public class RincianAnggaranNotFoundException extends RuntimeException {
    public RincianAnggaranNotFoundException(Long id) {
        super("Rincian anggaran dengan id " + id + " tidak ditemukan.");
    }
}
