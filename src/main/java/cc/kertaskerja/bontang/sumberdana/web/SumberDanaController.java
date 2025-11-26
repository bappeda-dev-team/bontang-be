package cc.kertaskerja.bontang.sumberdana.web;

import cc.kertaskerja.bontang.sumberdana.domain.SumberDana;
import cc.kertaskerja.bontang.sumberdana.domain.SumberDanaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "sumber dana")
@RequestMapping("sumberdana")
public class SumberDanaController {
    private final SumberDanaService sumberDanaService;

    public SumberDanaController(SumberDanaService sumberDanaService) {
        this.sumberDanaService = sumberDanaService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public SumberDana getById(@PathVariable("id") Long id) {
        return sumberDanaService.detailSumberDanaById(id);
    }

    /**
     * Ambil semua data sumber dana
     */
    @GetMapping("detail/findall")
    public Iterable<SumberDana> findAll() {
        return sumberDanaService.findAll();
    }

    /**
     * Ubah data sumber dana berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public SumberDana put(@PathVariable("id") Long id, @Valid @RequestBody SumberDanaRequest request) {
        SumberDana existingSumberDana = sumberDanaService.detailSumberDanaById(id);

        SumberDana sumberDana = new SumberDana(
                existingSumberDana.id(),
                request.kodeDanaLama(),
                request.sumberDana(),
                request.kodeDanaBaru(),
                request.setInput(),
                existingSumberDana.createdDate(),
                null
        );

        return sumberDanaService.ubahSumberDana(id, sumberDana);
    }

    /**
     * Tambah data sumber dana
     * @param request
     */
    @PostMapping
    public ResponseEntity<SumberDana> post(@Valid @RequestBody SumberDanaRequest request) {
        SumberDana sumberDana = SumberDana.of(
                request.kodeDanaLama(),
                request.sumberDana(),
                request.kodeDanaBaru(),
                request.setInput()
        );
        SumberDana saved = sumberDanaService.tambahSumberDana(sumberDana);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus sumber dana berdasarkan id sumber dana
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        sumberDanaService.hapusSumberDana(id);
    }
}
