package cc.kertaskerja.bontang.rincianbelanja.domain.exception;

public class RincianBelanjaNotFoundException extends RuntimeException {
    public RincianBelanjaNotFoundException(Long id) {
        super("Rincian belanja dengan id " + id + " tidak ditemukan");
    }

    public RincianBelanjaNotFoundException(String nipPegawai, String kodeOpd, Integer tahun) {
        super("Rincian belanja dengan nip " + nipPegawai + ", kode opd " + kodeOpd + ", dan tahun " + tahun + " tidak ditemukan");
    }

    public RincianBelanjaNotFoundException(Long id, String nipPegawai) {
        super("Rincian belanja dengan id " + id + ", nip pegawai " + nipPegawai + " tidak ditemukan");
    }
}