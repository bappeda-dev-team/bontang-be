package cc.kertaskerja.bontang.indikator.domain;

public class IndikatorNotFoundException extends RuntimeException {

    public IndikatorNotFoundException(Long id) {
        super("Indikator dengan ID " + id + " tidak ditemukan");
    }
}