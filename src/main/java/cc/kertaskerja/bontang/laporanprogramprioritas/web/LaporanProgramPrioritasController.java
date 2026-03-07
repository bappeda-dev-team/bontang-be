package cc.kertaskerja.bontang.laporanprogramprioritas.web;

import cc.kertaskerja.bontang.laporanprogramprioritas.domain.LaporanProgramPrioritasService;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.LaporanProgramPrioritasDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
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
            @RequestParam("tahun") Integer tahun,
            Authentication authentication
    ) {
        List<Long> idProgramPrioritasAnggaranList = Arrays.stream(idProgramPrioritasAnggaran.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
        String requesterNip = authentication.getName();
        boolean isLevel2 = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_LEVEL_2".equals(authority.getAuthority()));
        return laporanProgramPrioritasService.getLaporanProgramPrioritas(
                idProgramPrioritasAnggaranList,
                tahun,
                requesterNip,
                isLevel2
        );
    }

    @Operation(
            summary = "Get laporan program prioritas by pegawai (LEVEL_3)",
            description = "Mengambil data laporan program prioritas berdasarkan kode opd, tahun, dan nip pegawai"
    )
    @GetMapping("detail/kodeopd/{kodeOpd}/tahun/{tahun}/nip/{nipPegawai}")
    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritasByPegawai(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun,
            @PathVariable("nipPegawai") String nipPegawai
    ) {
        return laporanProgramPrioritasService.getLaporanProgramPrioritasByPegawai(
                kodeOpd,
                tahun,
                nipPegawai
        );
    }

    @Operation(
            summary = "Get laporan program prioritas semua opd",
            description = "Mengambil data laporan program prioritas untuk semua opd berdasarkan tahun"
    )
    @GetMapping("detail/all-opd")
    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritasAllOpd(
            @RequestParam("tahun") Integer tahun,
            Authentication authentication
    ) {
        String requesterNip = authentication.getName();
        boolean isLevel2 = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_LEVEL_2".equals(authority.getAuthority()));
        return laporanProgramPrioritasService.getLaporanProgramPrioritasAllOpd(
                tahun,
                requesterNip,
                isLevel2
        );
    }

    @Operation(
            summary = "Get laporan program prioritas by kode opd",
            description = "Mengambil data laporan program prioritas berdasarkan kode opd dan tahun"
    )
    @GetMapping("detail/kodeopd/{kodeOpd}/tahun/{tahun}")
    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritasByKodeOpdAndTahun(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun,
            Authentication authentication
    ) {
        String requesterNip = authentication.getName();
        boolean isLevel2 = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_LEVEL_2".equals(authority.getAuthority()));
        return laporanProgramPrioritasService.getLaporanProgramPrioritasByKodeOpd(
                kodeOpd,
                tahun,
                requesterNip,
                isLevel2
        );
    }

    @Operation(
            summary = "Get laporan program prioritas terverifikasi",
            description = "Mengambil data laporan program prioritas yang terverifikasi berdasarkan verifikator rencana kinerja; filterHash diabaikan"
    )
    @GetMapping("verified/detail/id_program_prioritas_anggaran={idProgramPrioritasAnggaran}")
    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritasVerified(
            @PathVariable("idProgramPrioritasAnggaran") String idProgramPrioritasAnggaran,
            @RequestParam(value = "filterHash", required = false) String filterHash,
            Authentication authentication
    ) {
        List<Long> idProgramPrioritasAnggaranList = Arrays.stream(idProgramPrioritasAnggaran.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();
        String requesterNip = authentication.getName();
        boolean isLevel2 = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_LEVEL_2".equals(authority.getAuthority()));
        return laporanProgramPrioritasService.getLaporanProgramPrioritasVerified(
                idProgramPrioritasAnggaranList,
                requesterNip,
                isLevel2,
                filterHash
        );
    }
}
