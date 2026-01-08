package cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.web;

import cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain.KodeRekeningRincianAnggaran;
import cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain.KodeRekeningRincianAnggaranService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "kode rekening rincian anggaran")
@RequestMapping("koderekeningrinciananggaran")
public class KodeRekeningRincianAnggaranController {
    private final KodeRekeningRincianAnggaranService kodeRekeningRincianAnggaranService;

    public KodeRekeningRincianAnggaranController(KodeRekeningRincianAnggaranService kodeRekeningRincianAnggaranService) {
        this.kodeRekeningRincianAnggaranService = kodeRekeningRincianAnggaranService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public KodeRekeningRincianAnggaran getById(@PathVariable("id") Long id) {
        return kodeRekeningRincianAnggaranService.detailKodeRekeningRincianAnggaranById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<KodeRekeningRincianAnggaran> findAll() {
        return kodeRekeningRincianAnggaranService.findAll();
    }

    /**
     * Ambil data berdasarkan idRincianAnggaran
     * @param idRincianAnggaran
     */
    @GetMapping("rinciananggaran/{idRincianAnggaran}")
    public Iterable<KodeRekeningRincianAnggaran> getByIdRincianAnggaran(@PathVariable("idRincianAnggaran") Integer idRincianAnggaran) {
        return kodeRekeningRincianAnggaranService.findByIdRincianAnggaran(idRincianAnggaran);
    }

    /**
     * Ubah data berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public KodeRekeningRincianAnggaran put(
            @PathVariable("id") Long id,
            @Valid @RequestBody KodeRekeningRincianAnggaranRequest request) {
        return kodeRekeningRincianAnggaranService.ubahKodeRekeningRincianAnggaran(id, request);
    }

    /**
     * Tambah data berdasarkan idRincianAnggaran
     * @param idRincianAnggaran
     * @param request
     */
    @PostMapping("rinciananggaran/{idRincianAnggaran}")
    public ResponseEntity<KodeRekeningRincianAnggaran> post(
            @PathVariable("idRincianAnggaran") Integer idRincianAnggaran,
            @Valid @RequestBody KodeRekeningRincianAnggaranRequest request) {
        KodeRekeningRincianAnggaran saved = kodeRekeningRincianAnggaranService.tambahKodeRekeningRincianAnggaran(idRincianAnggaran, request);
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
        kodeRekeningRincianAnggaranService.hapusKodeRekeningRincianAnggaran(id);
    }
}