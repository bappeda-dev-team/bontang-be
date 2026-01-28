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
            description = "Mengambil data laporan program prioritas rekap anggaran untuk banyak program prioritas anggaran berdasarkan id program prioritas anggaran (dipisahkan dengan koma) dan tahun")
    @GetMapping("detail/id_program_prioritas_anggaran={idProgramPrioritasAnggaran}")
    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritas(
            @PathVariable("idProgramPrioritasAnggaran") String idProgramPrioritasAnggaran,
            @RequestParam("tahun") Integer tahun
    ) {
        List<Long> idProgramPrioritasAnggaranList = Arrays.stream(idProgramPrioritasAnggaran.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
        return laporanProgramPrioritasService.getLaporanProgramPrioritas(idProgramPrioritasAnggaranList, tahun);
    }
}
