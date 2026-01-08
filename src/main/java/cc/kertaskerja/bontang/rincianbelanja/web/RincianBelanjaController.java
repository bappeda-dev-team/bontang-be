package cc.kertaskerja.bontang.rincianbelanja.web;

import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanjaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "rincian belanja")
@RequestMapping("rincianbelanja")
public class RincianBelanjaController {
    private final RincianBelanjaService rincianBelanjaService;

    public RincianBelanjaController(RincianBelanjaService rincianBelanjaService) {
        this.rincianBelanjaService = rincianBelanjaService;
    }

    @GetMapping("detail/pegawai/{nipPegawai}/kodeopd/{kodeOpd}/tahun/{tahun}")
    public ResponseEntity<Map<String, Object>> getByNipPegawaiAndKodeOpdAndTahun(
            @PathVariable("nipPegawai") String nipPegawai,
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun) {
        Map<String, Object> response = rincianBelanjaService.findByNipPegawaiAndKodeOpdAndTahun(nipPegawai, kodeOpd, tahun);
        return ResponseEntity.ok(response);
    }

    @GetMapping("detail/rincian-belanja/{idRincianBelanja}/pegawai/{nipPegawai}/input-rincian")
    public ResponseEntity<Map<String, Object>> getDetailByIdAndNipPegawai(
            @PathVariable("idRincianBelanja") Long idRincianBelanja,
            @PathVariable("nipPegawai") String nipPegawai) {
        Map<String, Object> response = rincianBelanjaService.findDetailByIdAndNipPegawai(idRincianBelanja, nipPegawai);
        return ResponseEntity.ok(response);
    }

    @PutMapping("update/{idRincianBelanja}")
    public ResponseEntity<Map<String, Object>> put(@PathVariable("idRincianBelanja") Long idRincianBelanja, @Valid @RequestBody RincianBelanjaRequest request) {
        
        return rincianBelanjaService.ubahRincianBelanja(idRincianBelanja, request);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> post(@Valid @RequestBody RincianBelanjaRequest request) {
        return rincianBelanjaService.tambahRincianBelanja(request);
    }

    @DeleteMapping("delete/{idRincianBelanja}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("idRincianBelanja") Long id) {
        rincianBelanjaService.hapusRincianBelanja(id);
    }
}
