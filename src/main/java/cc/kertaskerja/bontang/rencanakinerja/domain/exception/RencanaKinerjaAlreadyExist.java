package cc.kertaskerja.bontang.rencanakinerja.domain.exception;

public class RencanaKinerjaAlreadyExist extends RuntimeException {
    public RencanaKinerjaAlreadyExist(String nip, String kodeOpd, Integer tahun) {
        super("Rencana kinerja dengan nip " + nip + " kode opd" + kodeOpd +  " tahun" + tahun +  " sudah ada.");
    }
}
