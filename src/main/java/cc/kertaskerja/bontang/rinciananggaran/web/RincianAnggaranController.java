package cc.kertaskerja.bontang.rinciananggaran.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cc.kertaskerja.bontang.rinciananggaran.domain.RincianAnggaran;
import cc.kertaskerja.bontang.rinciananggaran.domain.RincianAnggaranService;

@RestController
@Tag(name = "rincian anggaran")
@RequestMapping("rinciananggaran")
public class RincianAnggaranController {
    private final RincianAnggaranService rincianAnggaranService;

    public RincianAnggaranController(RincianAnggaranService rincianAnggaranService) {
        this.rincianAnggaranService = rincianAnggaranService;
    }

    @GetMapping("detail/{id}")
    public RincianAnggaran getById(@PathVariable("id") Long id) {
        return rincianAnggaranService.detailRincianAnggaranById(id);
    }

    @GetMapping("detail/findall")
    public Iterable<RincianAnggaran> findAll() {
        return rincianAnggaranService.findAll();
    }

    @GetMapping("detail/rinciananggaran/{idRincianBelanja}")
    public Iterable<RincianAnggaran> findByRincianAnggaran(@PathVariable("idRincianBelanja") Integer idRincianAnggaran) {
        return rincianAnggaranService.findByIdRincianAnggaranOrderByUrutan(idRincianAnggaran);
    }

    @PutMapping("rincianBelanja/{idRincianBelanja}/update/{id}")
    public RincianAnggaran put(@PathVariable("idRincianBelanja") Integer idRincianBelanja,
                                                  @PathVariable("id") Long id,
                                                  @Valid @RequestBody RincianAnggaranRequest request) {
        return rincianAnggaranService.ubahRincianAnggaran(id, idRincianBelanja, request);
    }
    
    @PostMapping("{idRincianBelanja}")
    public ResponseEntity<RincianAnggaran> post(@PathVariable("idRincianBelanja") Integer idRincianBelanja,
                                             @Valid @RequestBody RincianAnggaranRequest request) {
        RincianAnggaran saved = rincianAnggaranService.tambahRincianAnggaran(idRincianBelanja, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // Hapus by id
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        rincianAnggaranService.hapusRincianAnggaran(id);
    }
}
