package cc.kertaskerja.bontang.laporanprogramprioritas.web.response;

import java.util.List;

public record LaporanProgramPrioritasDataResponse(
    Long id_program_prioritas,
    String nama_program_prioritas,
    Integer tahun,
    String kode_opd,
    String nama_opd,
    List<PelaksanaLaporanResponse> pelaksanas,
    String keterangan
) {}
