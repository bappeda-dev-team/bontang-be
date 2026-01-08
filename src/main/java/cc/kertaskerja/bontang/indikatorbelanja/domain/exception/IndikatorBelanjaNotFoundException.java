package cc.kertaskerja.bontang.indikatorbelanja.domain.exception;

public class IndikatorBelanjaNotFoundException extends RuntimeException{
    public IndikatorBelanjaNotFoundException(Long id) {
        super("Indikator belanja dengan ID " + id + " tidak ditemukan");
    }
}
