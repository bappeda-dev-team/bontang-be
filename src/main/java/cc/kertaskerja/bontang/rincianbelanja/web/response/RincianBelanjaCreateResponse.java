package cc.kertaskerja.bontang.rincianbelanja.web.response;

public record RincianBelanjaCreateResponse(
    Long id_rencana_aksi,
    String rencana_aksi,
    Integer anggaran,
    String kode_rekening,
    String nama_rekening
) {
}
