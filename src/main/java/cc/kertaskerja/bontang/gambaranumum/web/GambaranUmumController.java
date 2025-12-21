package cc.kertaskerja.bontang.gambaranumum.web;

import cc.kertaskerja.bontang.gambaranumum.domain.GambaranUmum;
import cc.kertaskerja.bontang.gambaranumum.domain.GambaranUmumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "gambaran umum")
@RequestMapping("gambaranumum")
public class GambaranUmumController {
    private final GambaranUmumService gambaranUmumService;

    public GambaranUmumController(GambaranUmumService gambaranUmumService) {
        this.gambaranUmumService = gambaranUmumService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public GambaranUmum getById(@PathVariable("id") Long id) {
        return gambaranUmumService.detailGambaranUmumById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<GambaranUmum> findAll() {
        return gambaranUmumService.findAll();
    }

    /**
     * Ubah data berdasarkan idRencanaKinerja
     * @param idRencanaKinerja
     */
    @PutMapping("update/{idRencanaKinerja}")
    public GambaranUmum put(@PathVariable("idRencanaKinerja") Long idRencanaKinerja, @Valid @RequestBody GambaranUmumRequest request) {
        GambaranUmum existingGambaranUmum = gambaranUmumService.detailGambaranUmumById(idRencanaKinerja);

        GambaranUmum gambaranUmum = new GambaranUmum(
                existingGambaranUmum.id(),
                request.gambaranUmum(),
                request.uraian(),
                request.kodeOpd(),
                request.tahun(),
                existingGambaranUmum.createdDate(),
                null
        );

        return gambaranUmumService.ubahGambaranUmum(idRencanaKinerja, gambaranUmum);
    }

    /**
     * Tambah data berdasarkan idRencanaKinerja
     * @param idRencanaKinerja
     * @param request
     */
    @PostMapping("{idRencanaKinerja}")
    public ResponseEntity<GambaranUmum> post(@PathVariable("idRencanaKinerja") Long idRencanaKinerja, @Valid @RequestBody GambaranUmumRequest request) {
        GambaranUmum gambaranUmum = GambaranUmum.of(
                request.gambaranUmum(),
                request.uraian(),
                request.kodeOpd(),
                request.tahun()
        );
        GambaranUmum saved = gambaranUmumService.tambahGambaranUmum(gambaranUmum);
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
        gambaranUmumService.hapusGambaranUmum(id);
    }
}