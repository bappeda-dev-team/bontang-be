package cc.kertaskerja.bontang.kegiatan.web;

import cc.kertaskerja.bontang.kegiatan.domain.Kegiatan;
import cc.kertaskerja.bontang.kegiatan.domain.KegiatanService;
import cc.kertaskerja.bontang.kegiatan.web.request.KegiatanBatchRequest;
import cc.kertaskerja.bontang.kegiatan.web.request.KegiatanRequest;
import cc.kertaskerja.bontang.kegiatan.web.response.KegiatanResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.StreamSupport;

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
    public List<KegiatanResponse> findAll() {
        Iterable<Kegiatan> kegiatans = kegiatanService.findAll();
        return StreamSupport.stream(kegiatans.spliterator(), false)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Ambil data kegiatan berdasarkan kumpulan kode kegiatan
     */
    @PostMapping("/find/batch")
    public List<Kegiatan> findBatch(@Valid @RequestBody KegiatanBatchRequest request) {
        return kegiatanService.detailKegiatanByKodeKegiatanIn(request.kodeKegiatan());
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
                request.namaKegiatan()
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

    private KegiatanResponse mapToResponse(Kegiatan kegiatan) {
        return new KegiatanResponse(
                kegiatan.id(),
                kegiatan.kodeKegiatan(),
                kegiatan.namaKegiatan(),
                kegiatan.createdDate(),
                kegiatan.lastModifiedDate()
        );
    }
}
