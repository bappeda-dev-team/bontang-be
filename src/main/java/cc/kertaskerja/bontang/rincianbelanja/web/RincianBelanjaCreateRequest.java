package cc.kertaskerja.bontang.rincianbelanja.web;

public record RincianBelanjaCreateRequest(
    Long id_rencana_aksi,
    Integer anggaran,
    String kode_rekening,
    String nama_rekening
) {
}
