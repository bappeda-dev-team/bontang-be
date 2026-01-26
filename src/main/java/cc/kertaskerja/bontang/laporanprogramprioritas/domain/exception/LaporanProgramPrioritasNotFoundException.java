package cc.kertaskerja.bontang.laporanprogramprioritas.domain.exception;

public class LaporanProgramPrioritasNotFoundException extends RuntimeException {
    public LaporanProgramPrioritasNotFoundException(Long id) {
        super("Laporan Program Prioritas dengan id " + id + " tidak ditemukan");
    }
}
