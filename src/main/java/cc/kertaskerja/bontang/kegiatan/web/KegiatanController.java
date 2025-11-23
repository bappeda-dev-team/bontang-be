package cc.kertaskerja.bontang.kegiatan.web;

import cc.kertaskerja.bontang.kegiatan.domain.Kegiatan;
import cc.kertaskerja.bontang.kegiatan.domain.KegiatanService;
import cc.kertaskerja.bontang.opd.domain.Opd;
import cc.kertaskerja.bontang.opd.domain.OpdService;
import cc.kertaskerja.bontang.opd.web.OpdRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "kegiatan")
@RequestMapping("kegiatan")
public class KegiatanController {
    private final KegiatanService kegiatanService;

    public KegiatanController(KegiatanService kegiatanService) {
        this.kegiatanService = kegiatanService;
    }

    /**
     * Ambil data berdasarkan kode kegiatan
     * @param kodeKegiatan
     */
    @GetMapping("detail/{kodeKegiatan}")
    public Kegiatan getByKodeKegiatan(@PathVariable("kodeKegiatan") String kodeKegiatan) {
        return kegiatanService.detailKegiatanByKodeKegiatan(kodeKegiatan);
    }

    /**
     * Ambil semua data kegiatan
     */
    @GetMapping("detail/findall")
    public Iterable<Kegiatan> findAll() {
        return kegiatanService.findAll();
    }

    /**
     * Ubah data opd berdasarkan kode kegiatan
     * @param kodeKegiatan
     */
    @PutMapping("update/{kodeKegiatan}")
    public Kegiatan put(@PathVariable("kodeKegiatan") String kodeKegiatan, @Valid @RequestBody KegiatanRequest request) {
        Kegiatan existingKegiatan = kegiatanService.detailKegiatanByKodeKegiatan(kodeKegiatan);

        Kegiatan kegiatan = new Kegiatan(
                existingKegiatan.id(),
                request.kodeKegiatan(),
                request.namaKegiatan(),
                request.kodeOpd(),
                request.kodeProgram(),
                existingKegiatan.createdDate(),
                null
        );

        return kegiatanService.ubahKegiatan(kodeKegiatan, kegiatan);
    }

    /**
     * Tambah data kegiatan
     * @param request
     */
    @PostMapping
    public ResponseEntity<Kegiatan> post(@Valid @RequestBody KegiatanRequest request) {
        Kegiatan kegiatan = Kegiatan.of(
                request.kodeKegiatan(),
                request.namaKegiatan(),
                request.kodeOpd(),
                request.kodeProgram()
        );
        Kegiatan saved = kegiatanService.tambahKegiatan(kegiatan);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus kegiatan berdasarkan kode kegiatan
     * @param kodeKegiatan
     */
    @DeleteMapping("delete/{kodeKegiatan}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeKegiatan") String kodeKegiatan) {
        kegiatanService.hapusKegiatan(kodeKegiatan);
    }
}
