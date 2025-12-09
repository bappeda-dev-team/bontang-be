package cc.kertaskerja.bontang.rencanakinerja.web;

import java.net.URI;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaService;
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
    @GetMapping("detail/{id}")
    public RencanaKinerja getById(@PathVariable("id") Long id) {
        return rencanaKinerjaService.detailRencanaKinerjaById(id);
    }

    /**
     * Ambil semua data rencana kinerja
     */
    @GetMapping("detail/findall")
    public Iterable<RencanaKinerja> findAll() {
        return rencanaKinerjaService.findAll();
    }

    /**
     * Ubah data rencana kinerja berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public RencanaKinerja put(@PathVariable("id") Long id, @Valid @RequestBody RencanaKinerjaRequest request) {
        RencanaKinerja existingRencanaKinerja = rencanaKinerjaService.detailRencanaKinerjaById(id);

        RencanaKinerja rencanaKinerja = new RencanaKinerja(
                existingRencanaKinerja.id(),
                request.rencanaKinerja(),
                request.indikator(),
                request.target(),
                request.sumberDana(),
                request.keterangan(),
                existingRencanaKinerja.createdDate(),
                null
        );

        return rencanaKinerjaService.ubahRencanaKinerja(id, rencanaKinerja);
    }

    /**
     * Tambah data rencana kinerja
     * @param request
     */
    @PostMapping
    public ResponseEntity<RencanaKinerja> post(@Valid @RequestBody RencanaKinerjaRequest request) {
        RencanaKinerja rencanaKinerja = RencanaKinerja.of(
                request.rencanaKinerja(),
                request.indikator(),
                request.target(),
                request.sumberDana(),
                request.keterangan()
        );
        RencanaKinerja saved = rencanaKinerjaService.tambahRencanaKinerja(rencanaKinerja);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus rencana kinerja berdasarkan id rencana kinerja
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        rencanaKinerjaService.hapusRencanaKinerja(id);
    }
}
