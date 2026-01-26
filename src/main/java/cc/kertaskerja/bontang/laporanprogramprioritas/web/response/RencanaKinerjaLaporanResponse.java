package cc.kertaskerja.bontang.laporanprogramprioritas.web.response;

public record RencanaKinerjaLaporanResponse(
    Long id_rencana_kinerja,
    String rencana_kinerja,
    String nama_pelaksana,
    String nip_pelaksana,
    String kode_subkegiatan,
    String nama_subkegiatan,
    Integer pagu,
    TahapanPelaksanaanResponse tahapan_pelaksanaan
) {}
