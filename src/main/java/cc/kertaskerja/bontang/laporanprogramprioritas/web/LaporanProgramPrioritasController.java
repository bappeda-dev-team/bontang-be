package cc.kertaskerja.bontang.laporanprogramprioritas.web;

import cc.kertaskerja.bontang.laporanprogramprioritas.domain.LaporanProgramPrioritasService;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.LaporanProgramPrioritasDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@Tag(name = "laporan program prioritas")
@RequestMapping("laporanprogramprioritas")
public class LaporanProgramPrioritasController {
    private final LaporanProgramPrioritasService laporanProgramPrioritasService;

    public LaporanProgramPrioritasController(LaporanProgramPrioritasService laporanProgramPrioritasService) {
        this.laporanProgramPrioritasService = laporanProgramPrioritasService;
    }

    @Operation(summary = "Get laporan program prioritas multiple rekap anggaran",
            description = "Mengambil data laporan program prioritas rekap anggaran untuk banyak program prioritas berdasarkan id program prioritas (dipisahkan dengan koma), kode OPD, dan tahun")
    @GetMapping("batch/{idProgramPrioritas}")
    public List<LaporanProgramPrioritasDataResponse> getLaporanBatch(
            @PathVariable("idProgramPrioritas") String idProgramPrioritas,
            @RequestParam("kode_opd") String kode_opd,
            @RequestParam("tahun") Integer tahun
    ) {
        List<Long> idProgramPrioritasList = Arrays.stream(idProgramPrioritas.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
        return laporanProgramPrioritasService.getLaporanBatch(idProgramPrioritasList, kode_opd, null, tahun);
    }
}
