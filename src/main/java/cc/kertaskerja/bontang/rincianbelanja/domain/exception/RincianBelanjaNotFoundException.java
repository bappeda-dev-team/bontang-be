package cc.kertaskerja.bontang.rincianbelanja.domain.exception;

public class RincianBelanjaNotFoundException extends RuntimeException {
    public RincianBelanjaNotFoundException(Long id) {
        super("Rincian Belanja dengan id " + id + " tidak ditemukan");
    }

    public RincianBelanjaNotFoundException(String nipPegawai, String kodeOpd, Integer tahun) {
        super("Rincian Belanja dengan kode nip pegawai " + nipPegawai + " dan kode opd " + kodeOpd +  " dan tahun " + tahun + " tidak ditemukan");
    }
}
