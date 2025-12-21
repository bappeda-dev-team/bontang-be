package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain;

public class PelaksanaanNotFoundException extends RuntimeException {
    public PelaksanaanNotFoundException(Long id) {
        super("Pelaksanaan rencana aksi dengan id " + id + " tidak ditemukan");
    }
}
