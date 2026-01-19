package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain.exception;

public class BobotExceededException extends RuntimeException {
    public BobotExceededException(Integer idRencanaAksi, Integer bobotDiminta, Integer bobotTersedia) {
        super("Bobot yang diminta (" + bobotDiminta + ") melebihi bobot tersedia (" + bobotTersedia + ") untuk rencana aksi id " + idRencanaAksi);
    }
}
