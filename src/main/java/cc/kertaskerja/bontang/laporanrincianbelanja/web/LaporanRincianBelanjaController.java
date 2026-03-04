package cc.kertaskerja.bontang.laporanrincianbelanja.web;

import cc.kertaskerja.bontang.laporanrincianbelanja.domain.LaporanRincianBelanjaService;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaAllOpdResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaEnvelopeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "laporan rincian belanja")
@RequestMapping("laporanrincianbelanja")
public class LaporanRincianBelanjaController {
    private final LaporanRincianBelanjaService laporanRincianBelanjaService;

    public LaporanRincianBelanjaController(LaporanRincianBelanjaService laporanRincianBelanjaService) {
        this.laporanRincianBelanjaService = laporanRincianBelanjaService;
    }

    @GetMapping("detail/kodeopd/{kodeOpd}/tahun/{tahun}")
    public LaporanRincianBelanjaEnvelopeResponse getLaporanRincianBelanja(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun,
            Authentication authentication
    ) {
        String requesterNip = authentication.getName();
        boolean isLevel2 = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_LEVEL_2".equals(authority.getAuthority()));
        return laporanRincianBelanjaService.getLaporanRincianBelanja(kodeOpd, tahun, requesterNip, isLevel2);
    }

    @Operation(
            summary = "Get laporan rincian belanja by pegawai (LEVEL_3)",
            description = "Mengambil data laporan rincian belanja berdasarkan kode opd, tahun, dan nip pegawai"
    )
    @GetMapping("detail/kodeopd/{kodeOpd}/tahun/{tahun}/nip/{nipPegawai}")
    public LaporanRincianBelanjaEnvelopeResponse getLaporanRincianBelanjaByPegawai(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun,
            @PathVariable("nipPegawai") String nipPegawai
    ) {
        return laporanRincianBelanjaService.getLaporanRincianBelanjaByPegawai(
                kodeOpd,
                tahun,
                nipPegawai
        );
    }

    @Operation(
            summary = "Get laporan rincian belanja semua opd",
            description = "Mengambil data laporan rincian belanja untuk semua opd berdasarkan tahun"
    )
    @GetMapping("detail/all-opd/tahun/{tahun}")
    public LaporanRincianBelanjaAllOpdResponse getLaporanRincianBelanjaAllOpd(
            @PathVariable("tahun") Integer tahun,
            Authentication authentication
    ) {
        String requesterNip = authentication.getName();
        boolean isLevel2 = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_LEVEL_2".equals(authority.getAuthority()));
        return laporanRincianBelanjaService.getLaporanRincianBelanjaAllOpd(
                tahun,
                requesterNip,
                isLevel2
        );
    }

    @Operation(
            summary = "Get laporan rincian belanja terverifikasi",
            description = "Mengambil data laporan rincian belanja yang terverifikasi berdasarkan verifikator rencana kinerja; filterHash diabaikan"
    )
    @GetMapping("verified/detail/rencanaKinerja/{idRencanaKinerja}")
    public LaporanRincianBelanjaEnvelopeResponse getLaporanRincianBelanjaVerified(
            @PathVariable("idRencanaKinerja") Long idRencanaKinerja,
            @RequestParam(value = "filterHash", required = false) String filterHash,
            Authentication authentication
    ) {
        String requesterNip = authentication.getName();
        boolean isLevel2 = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_LEVEL_2".equals(authority.getAuthority()));
        return laporanRincianBelanjaService.getLaporanRincianBelanjaVerified(
                idRencanaKinerja,
                requesterNip,
                isLevel2,
                filterHash
        );
    }
}
