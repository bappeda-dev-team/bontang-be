package cc.kertaskerja.bontang.dasarhukum.web;

import cc.kertaskerja.bontang.dasarhukum.domain.DasarHukum;
import cc.kertaskerja.bontang.dasarhukum.domain.DasarHukumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "dasar hukum")
@RequestMapping("dasarhukum")
public class DasarHukumController {
    private final DasarHukumService dasarHukumService;

    public DasarHukumController(DasarHukumService dasarHukumService) {
        this.dasarHukumService = dasarHukumService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public DasarHukum getById(@PathVariable("id") Long id) {
        return dasarHukumService.detailDasarHukumById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<DasarHukum> findAll() {
        return dasarHukumService.findAll();
    }

    /**
     * Ubah data berdasarkan idRencanaKinerja
     * @param idRencanaKinerja
     */
    @PutMapping("update/{idRencanaKinerja}")
    public DasarHukum put(@PathVariable("idRencanaKinerja") Long idRencanaKinerja, @Valid @RequestBody DasarHukumRequest request) {
        DasarHukum existingDasarHukum = dasarHukumService.detailDasarHukumById(idRencanaKinerja);

        DasarHukum dasarHukum = new DasarHukum(
                existingDasarHukum.id(),
                request.peraturanTerkait(),
                request.uraian(),
                request.kodeOpd(),
                request.tahun(),
                existingDasarHukum.createdDate(),
                null
        );

        return dasarHukumService.ubahDasarHukum(idRencanaKinerja, dasarHukum);
    }

    /**
     * Tambah data berdasarkan idRencanaKinerja
     * @param idRencanaKinerja
     * @param request
     */
    @PostMapping("{idRencanaKinerja}")
    public ResponseEntity<DasarHukum> post(@PathVariable("idRencanaKinerja") Long idRencanaKinerja, @Valid @RequestBody DasarHukumRequest request) {
        DasarHukum dasarHukum = DasarHukum.of(
                request.peraturanTerkait(),
                request.uraian(),
                request.kodeOpd(),
                request.tahun()
        );
        DasarHukum saved = dasarHukumService.tambahDasarHukum(dasarHukum);
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
        dasarHukumService.hapusDasarHukum(id);
    }
}
