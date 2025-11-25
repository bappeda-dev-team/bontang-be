package cc.kertaskerja.bontang.subkegiatan.web;

import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatan;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "subkegiatan")
@RequestMapping("subkegiatan")
public class SubKegiatanController {
    private final SubKegiatanService subKegiatanService;

    public SubKegiatanController(SubKegiatanService subKegiatanService) {
        this.subKegiatanService = subKegiatanService;
    }

    /**
     * Ambil data berdasarkan kode sub kegiatan
     * @param kodeSubKegiatan
     */
    @GetMapping("detail/{kodeSubKegiatan}")
    public SubKegiatan getByKodeSubKegiatan(@PathVariable("kodeSubKegiatan") String kodeSubKegiatan) {
        return subKegiatanService.detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan);
    }

    /**
     * Ambil semua data sub kegiatan
     */
    @GetMapping("detail/findall")
    public Iterable<SubKegiatan> findAll() {
        return subKegiatanService.findAll();
    }

    /**
     * Ubah data sub kegiatan berdasarkan kode sub kegiatan
     * @param kodeSubKegiatan
     */
    @PutMapping("update/{kodeSubKegiatan}")
    public SubKegiatan put(@PathVariable("kodeSubKegiatan") String kodeSubKegiatan, @Valid @RequestBody SubKegiatanRequest request) {
        SubKegiatan existingSubKegiatan = subKegiatanService.detailSubKegiatanByKodeSubKegiatan(kodeSubKegiatan);

        SubKegiatan subKegiatan = new SubKegiatan(
                existingSubKegiatan.id(),
                request.kodeSubKegiatan(),
                request.namaSubKegiatan(),
                existingSubKegiatan.createdDate(),
                null
        );

        return subKegiatanService.ubahSubKegiatan(kodeSubKegiatan, subKegiatan);
    }

    /**
     * Tambah data sub kegiatan
     * @param request
     */
    @PostMapping
    public ResponseEntity<SubKegiatan> post(@Valid @RequestBody SubKegiatanRequest request) {
        SubKegiatan subKegiatan = SubKegiatan.of(
                request.kodeSubKegiatan(),
                request.namaSubKegiatan()
        );
        SubKegiatan saved = subKegiatanService.tambahSubKegiatan(subKegiatan);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus sub kegiatan berdasarkan kode sub kegiatan
     * @param kodeSubKegiatan
     */
    @DeleteMapping("delete/{kodeSubKegiatan}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeSubKegiatan") String kodeSubKegiatan) {
        subKegiatanService.hapusSubKegiatan(kodeSubKegiatan);
    }
}
