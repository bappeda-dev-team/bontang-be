package cc.kertaskerja.bontang.rencanakinerja.web;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaService;
import cc.kertaskerja.bontang.rencanakinerja.web.RencanaKinerjaRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
@Tag(name = "rencana kinerja")
@RequestMapping("rencanakinerja")
public class RencanaKinerjaController {
    private final RencanaKinerjaService rencanaKinerjaService;

    public RencanaKinerjaController(RencanaKinerjaService rencanaKinerjaService) {
        this.rencanaKinerjaService = rencanaKinerjaService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    // @GetMapping("detail/{id}")
    // public RencanaKinerja getById(@PathVariable("id") Long id) {
    //     return rencanaKinerjaService.detailRencanaKinerjaById(id);
    // }

    /**
     * Ambil semua data rencana kinerja
     */
    @GetMapping("detail/findall")
    public Iterable<RencanaKinerja> findAll() {
        return rencanaKinerjaService.findAll();
    }

    /**
     * Ambil data rencana kinerja berdasarkan nip pegawai, kode opd, dan tahun
     * @param nipPegawai
     * @param kodeOpd
     * @param tahun
     */
    @GetMapping("detail/pegawai/{nipPegawai}/kodeopd/{kodeOpd}/tahun/{tahun}")
    public ResponseEntity<Map<String, Object>> getByNipPegawaiAndKodeOpdAndTahun(
            @PathVariable("nipPegawai") String nipPegawai,
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun) {
        Map<String, Object> response = rencanaKinerjaService.findByNipPegawaiAndKodeOpdAndTahun(nipPegawai, kodeOpd, tahun);
        return ResponseEntity.ok(response);
    }

    /**
     * Ubah data rencana kinerja berdasarkan id
     * @param idRencanaKinerja
     * @param request
     */
    @PutMapping("update/{idRencanaKinerja}")
    public ResponseEntity<Map<String, Object>> put(@PathVariable("idRencanaKinerja") Long idRencanaKinerja, @Valid @RequestBody RencanaKinerjaRequest request) {
        
        return rencanaKinerjaService.ubahRencanaKinerja(idRencanaKinerja, request);
    }

    /**
     * Tambah data rencana kinerja
     * @param request
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> post(@Valid @RequestBody RencanaKinerjaRequest request) {
        return rencanaKinerjaService.tambahRencanaKinerja(request);
    }

    /**
     * Hapus rencana kinerja berdasarkan id rencana kinerja
     * @param id
     */
    @DeleteMapping("delete/{idRencanaKinerja}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("idRencanaKinerja") Long id) {
        rencanaKinerjaService.hapusRencanaKinerja(id);
    }
}
