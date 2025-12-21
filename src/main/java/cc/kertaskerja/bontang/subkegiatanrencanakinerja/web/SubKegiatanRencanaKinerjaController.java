package cc.kertaskerja.bontang.subkegiatanrencanakinerja.web;

import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerjaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "subkegiatan rencana kinerja")
@RequestMapping("subkegiatanrencanakinerja")
public class SubKegiatanRencanaKinerjaController {
    private final SubKegiatanRencanaKinerjaService subKegiatanRencanaKinerjaService;

    public SubKegiatanRencanaKinerjaController(SubKegiatanRencanaKinerjaService subKegiatanRencanaKinerjaService) {
        this.subKegiatanRencanaKinerjaService = subKegiatanRencanaKinerjaService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public SubKegiatanRencanaKinerja getById(@PathVariable("id") Long id) {
        return subKegiatanRencanaKinerjaService.detailSubKegiatanRencanaKinerjaById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<SubKegiatanRencanaKinerja> findAll() {
        return subKegiatanRencanaKinerjaService.findAll();
    }

    /**
     * Tambah data
     * @param request
     * @param idRencanaKinerja
     */
    @PostMapping("{idRencanaKinerja}")
    public ResponseEntity<SubKegiatanRencanaKinerja> post(@PathVariable("idRencanaKinerja") Long idRencanaKinerja, @Valid @RequestBody SubKegiatanRencanaKinerjaRequest request) {
        // Buat request baru dengan idRencanaKinerja dari path variable
        SubKegiatanRencanaKinerjaRequest newRequest = new SubKegiatanRencanaKinerjaRequest(
            request.subKegiatanRencanaKinerjaId(),
            idRencanaKinerja.intValue(),
            request.kodeSubKegiatanRencanaKinerja(),
            request.namaSubKegiatanRencanaKinerja()
        );
        
        SubKegiatanRencanaKinerja saved = subKegiatanRencanaKinerjaService.tambahSubKegiatanRencanaKinerja(newRequest);
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
        subKegiatanRencanaKinerjaService.hapusSubKegiatanRencanaKinerja(id);
    }
}
