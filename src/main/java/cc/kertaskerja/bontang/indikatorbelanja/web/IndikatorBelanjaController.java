package cc.kertaskerja.bontang.indikatorbelanja.web;

import java.net.URI;

import cc.kertaskerja.bontang.indikatorbelanja.domain.IndikatorBelanja;
import cc.kertaskerja.bontang.indikatorbelanja.domain.IndikatorBelanjaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "indikator belanja")
@RequestMapping("indikatorbelanja")
public class IndikatorBelanjaController {
    private final IndikatorBelanjaService indikatorBelanjaService;

    public IndikatorBelanjaController(IndikatorBelanjaService indikatorBelanjaService) {
        this.indikatorBelanjaService = indikatorBelanjaService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public IndikatorBelanja getById(@PathVariable("id") Long id) {
        return indikatorBelanjaService.detailIndikatorBelanjaById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<IndikatorBelanja> findAll() {
        return indikatorBelanjaService.findAll();
    }

    /**
     * Ubah data berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public IndikatorBelanja put(@PathVariable("id") Long id, @Valid @RequestBody IndikatorBelanjaRequest request) {
        return indikatorBelanjaService.ubahIndikatorBelanja(id, request);
    }

    /**
     * Tambah data
     * @param request
     */
    @PostMapping
    public ResponseEntity<IndikatorBelanja> post(@Valid @RequestBody IndikatorBelanjaRequest request) {
        IndikatorBelanja saved = indikatorBelanjaService.tambahIndikatorBelanja(request);
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
        indikatorBelanjaService.hapusIndikatorBelanja(id);
    }
}
