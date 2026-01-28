package cc.kertaskerja.bontang.laporanrincianbelanja.web;

import cc.kertaskerja.bontang.laporanrincianbelanja.domain.LaporanRincianBelanjaService;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaEnvelopeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "laporan rincian belanja")
@RequestMapping("laporanrincianbelanja")
public class LaporanRincianBelanjaController {
    private final LaporanRincianBelanjaService laporanRincianBelanjaService;

    public LaporanRincianBelanjaController(LaporanRincianBelanjaService laporanRincianBelanjaService) {
        this.laporanRincianBelanjaService = laporanRincianBelanjaService;
    }

    @GetMapping("detail/pegawai/{nipPegawai}/{kodeOpd}/{tahun}")
    public LaporanRincianBelanjaEnvelopeResponse getLaporanRincianBelanja(
            @PathVariable("nipPegawai") String nipPegawai,
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun
    ) {
        return laporanRincianBelanjaService.getLaporanRincianBelanja(nipPegawai, kodeOpd, tahun);
    }
}
