package cc.kertaskerja.bontang.laporanverifikasi.web;

import cc.kertaskerja.bontang.laporanverifikasi.domain.LaporanVerifikasiService;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.LaporanProgramPrioritasDataResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaEnvelopeResponse;
import cc.kertaskerja.bontang.laporanverifikasi.web.response.LaporanCetakResponse;
import cc.kertaskerja.bontang.laporanverifikasi.web.response.LaporanVerifikasiResultResponse;
import cc.kertaskerja.bontang.laporanverifikasi.web.response.LaporanVerifikasiStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "laporan verifikasi")
@RequestMapping("laporan")
public class LaporanVerifikasiController {
    private final LaporanVerifikasiService laporanVerifikasiService;

    public LaporanVerifikasiController(LaporanVerifikasiService laporanVerifikasiService) {
        this.laporanVerifikasiService = laporanVerifikasiService;
    }

    @PostMapping("verifikasi")
    public LaporanVerifikasiResultResponse verifikasi(
            @Valid @RequestBody LaporanVerifikasiRequest request,
            Authentication authentication
    ) {
        return laporanVerifikasiService.verifikasiLaporan(request, authentication);
    }

    @GetMapping("verifikasi/status")
    public LaporanVerifikasiStatusResponse status(
            @RequestParam("jenisLaporan") String jenisLaporan,
            @RequestParam("kodeOpd") String kodeOpd,
            @RequestParam("tahun") Integer tahun,
            @RequestParam(value = "filterHash", required = false) String filterHash
    ) {
        return laporanVerifikasiService.getStatus(jenisLaporan, kodeOpd, tahun, filterHash);
    }

    @GetMapping("cetak")
    public LaporanCetakResponse cetak(
            @RequestParam("jenisLaporan") String jenisLaporan,
            @RequestParam("kodeOpd") String kodeOpd,
            @RequestParam("tahun") Integer tahun,
            @RequestParam(value = "filterHash", required = false) String filterHash,
            Authentication authentication
    ) {
        return laporanVerifikasiService.getCetak(
                jenisLaporan,
                kodeOpd,
                tahun,
                filterHash,
                authentication
        );
    }

    @Operation(
            summary = "Get laporan program prioritas terverifikasi (LEVEL_1)",
            description = "Mengambil data laporan program prioritas jika sudah terverifikasi berdasarkan laporan/verifikasi/status."
    )
    @GetMapping("verified/program-prioritas")
    public List<LaporanProgramPrioritasDataResponse> getVerifiedProgramPrioritas(
            @RequestParam("kodeOpd") String kodeOpd,
            @RequestParam("tahun") Integer tahun,
            @RequestParam(value = "filterHash", required = false) String filterHash,
            Authentication authentication
    ) {
        return laporanVerifikasiService.getVerifiedProgramPrioritas(
                kodeOpd,
                tahun,
                filterHash,
                authentication
        );
    }

    @Operation(
            summary = "Get laporan rincian belanja terverifikasi (LEVEL_1)",
            description = "Mengambil data laporan rincian belanja jika sudah terverifikasi berdasarkan laporan/verifikasi/status."
    )
    @GetMapping("verified/rincian-belanja")
    public LaporanRincianBelanjaEnvelopeResponse getVerifiedRincianBelanja(
            @RequestParam("kodeOpd") String kodeOpd,
            @RequestParam("tahun") Integer tahun,
            @RequestParam(value = "filterHash", required = false) String filterHash,
            Authentication authentication
    ) {
        return laporanVerifikasiService.getVerifiedRincianBelanja(
                kodeOpd,
                tahun,
                filterHash,
                authentication
        );
    }
}
