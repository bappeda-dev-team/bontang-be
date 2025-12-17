package cc.kertaskerja.bontang.indikator.web;

import cc.kertaskerja.bontang.indikator.domain.Indikator;
import cc.kertaskerja.bontang.indikator.domain.IndikatorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "indikator")
@RequestMapping("indikator")
public class IndikatorController {
    private final IndikatorService indikatorService;

    public IndikatorController(IndikatorService indikatorService) {
        this.indikatorService = indikatorService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public Indikator getById(@PathVariable("id") Long id) {
        return indikatorService.detailIndikatorById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<Indikator> findAll() {
        return indikatorService.findAll();
    }

    /**
     * Ubah data berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public Indikator put(@PathVariable("id") Long id, @Valid @RequestBody IndikatorRequest request) {
        Indikator existingIndikator = indikatorService.detailIndikatorById(id);

        Indikator indikator = new Indikator(
                existingIndikator.id(),
                request.namaIndikator(),
                existingIndikator.createdDate(),
                null
        );

        return indikatorService.ubahIndikator(id, indikator);
    }

    /**
     * Tambah data
     * @param request
     */
    @PostMapping
    public ResponseEntity<Indikator> post(@Valid @RequestBody IndikatorRequest request) {
        Indikator indikator = Indikator.of(
                request.namaIndikator()
        );
        Indikator saved = indikatorService.tambahIndikator(indikator);
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
        indikatorService.hapusIndikator(id);
    }
}