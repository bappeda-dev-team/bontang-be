package cc.kertaskerja.bontang.rencanakinerja.domain.exception;

public class RencanaKinerjaNotFoundException extends RuntimeException {
    public RencanaKinerjaNotFoundException(String nipPegawai, String kodeOpd, Integer tahun) {
        super("Rencana kinerja dengan nip " + nipPegawai + " kode opd " + kodeOpd + " tahun " + tahun + " tidak ditemukan.");
    }

    public RencanaKinerjaNotFoundException(Long id) {
        super("Rencana kinerja dengan id " + id + " tidak ditemukan.");
    }

    public RencanaKinerjaNotFoundException(String message) {
        super(message);
    }
}
