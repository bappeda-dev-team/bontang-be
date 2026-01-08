package cc.kertaskerja.bontang.targetbelanja.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cc.kertaskerja.bontang.targetbelanja.domain.TargetBelanja;
import cc.kertaskerja.bontang.targetbelanja.domain.TargetBelanjaService;

@RestController
@Tag(name = "target belanja")
@RequestMapping("targetbelanja")
public class TargetBelanjaController {
    private final TargetBelanjaService targetBelanjaService;

    public TargetBelanjaController(TargetBelanjaService targetBelanjaService) {
        this.targetBelanjaService = targetBelanjaService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public TargetBelanja getById(@PathVariable("id") Long id) {
        return targetBelanjaService.detailTargetBelanjaById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<TargetBelanja> findAll() {
        return targetBelanjaService.findAll();
    }

    /**
     * Ubah data berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public TargetBelanja put(@PathVariable("id") Long id, @Valid @RequestBody TargetBelanjaRequest request) {
        return targetBelanjaService.ubahTargetBelanja(id, request);
    }

    /**
     * Tambah data
     * @param request
     */
    @PostMapping
    public ResponseEntity<TargetBelanja> post(@Valid @RequestBody TargetBelanjaRequest request) {
        TargetBelanja saved = targetBelanjaService.tambahTargetBelanja(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus berdasarkan id
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        targetBelanjaService.hapusTargetBelanja(id);
    }
}
