package cc.kertaskerja.bontang.rincianbelanja.web;

import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanja;
import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanjaService;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaCreateResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "rincian belanja")
@RequestMapping("rincianbelanja")
public class RincianBelanjaController {
    private final RincianBelanjaService rincianBelanjaService;

    private RincianBelanjaController(RincianBelanjaService rincianBelanjaService) {
        this.rincianBelanjaService = rincianBelanjaService;
    }

    @GetMapping("detail/{idSubKegiatanRencanaKinerja}")
    public List<RincianBelanja> getById(@PathVariable("idSubKegiatanRencanaKinerja") Long id) {
        return rincianBelanjaService.findBySubkegiatanRencanaKinerjaId(id);
    }

    @GetMapping("detail/pegawai/{nipPegawai}/{kodeOpd}/{tahun}")
    public RincianBelanjaResponse getByNipPegawaiKodeOpdAndTahun(
            @PathVariable("nipPegawai") String nipPegawai,
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun) {
        return rincianBelanjaService.findByNipPegawaiKodeOpdAndTahun(nipPegawai, kodeOpd, tahun);
    }

    @PostMapping("upsert")
    public ResponseEntity<RincianBelanjaCreateResponse> upsertRincianBelanja(
            @Valid @RequestBody RincianBelanjaCreateRequest request) {
        RincianBelanja updated = rincianBelanjaService.upsertRincianBelanja(
                request.id_rencana_aksi(), 
                request.anggaran()
        );
        
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        
        RincianBelanjaCreateResponse response = new RincianBelanjaCreateResponse(
                updated.idRencanaAksi(),
                updated.rencanaAksi(),
                updated.anggaran()
        );
        
        return ResponseEntity.ok(response);
    }

    
}
