package cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.domain.exception;

public class PelaksanaanRincianAnggaranNotFoundException extends RuntimeException {
    public PelaksanaanRincianAnggaranNotFoundException(Long id) {
        super("Pelaksanaan rincian anggaran dengan id " + id + " tidak ditemukan.");
    }
}
