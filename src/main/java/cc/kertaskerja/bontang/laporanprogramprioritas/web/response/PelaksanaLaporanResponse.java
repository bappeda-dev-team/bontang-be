package cc.kertaskerja.bontang.laporanprogramprioritas.web.response;

import java.util.List;

public record PelaksanaLaporanResponse(
    String nama_pelaksana,
    String nip_pelaksana,
    List<RencanaKinerjaLaporanResponse> rencana_kinerjas
) {}
