package cc.kertaskerja.bontang.opd.web;

import cc.kertaskerja.bontang.opd.domain.Opd;
import cc.kertaskerja.bontang.opd.domain.OpdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "opd")
@RequestMapping("opd")
public class OpdController {
    private final OpdService opdService;

    public OpdController(OpdService opdService) {
        this.opdService = opdService;
    }

    /**
     * Ambil data berdasarkan kode opd
     * @param kodeOpd
     */
    @GetMapping("detail/{kodeOpd}")
    public Opd getByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        return opdService.detailOpdByKodeOpd(kodeOpd);
    }

    /**
     * Ambil semua data opd
     */
    @GetMapping("detail/findall")
    public Iterable<Opd> findAll() {
        return opdService.findAll();
    }

    /**
     * Ubah data opd berdasarkan kode opd
     * @param kodeOpd
     */
    @PutMapping("update/{kodeOpd}")
    public Opd put(@PathVariable("kodeOpd") String kodeOpd, @Valid @RequestBody OpdRequest request) {
        Opd existingOpd = opdService.detailOpdByKodeOpd(kodeOpd);

        Opd opd = new Opd(
                existingOpd.id(),
                request.kodeOpd(),
                request.namaOpd(),
                existingOpd.createdDate(),
                null
        );

        return opdService.ubahOpd(kodeOpd, opd);
    }

    /**
     * Tambah data opd
     * @param request
     */
    @PostMapping
    public ResponseEntity<Opd> post(@Valid @RequestBody OpdRequest request) {
        Opd opd = Opd.of(
                request.kodeOpd(),
                request.namaOpd()
        );
        Opd saved = opdService.tambahOpd(opd);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus opd berdasarkan kode opd
     * @param kodeOpd
     */
    @DeleteMapping("delete/{kodeOpd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeOpd") String kodeOpd) {
        opdService.hapusOpd(kodeOpd);
    }
}
