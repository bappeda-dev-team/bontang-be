package cc.kertaskerja.bontang.subkegiatanrincianbelanja.web;

import cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain.SubKegiatanRincianBelanja;
import cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain.SubKegiatanRincianBelanjaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "sub kegiatan rincian belanja")
@RequestMapping("subkegiatanrincianbelanja")
public class SubKegiatanRincianBelanjaController {
    private final SubKegiatanRincianBelanjaService subKegiatanRincianBelanjaService;

    public SubKegiatanRincianBelanjaController(SubKegiatanRincianBelanjaService subKegiatanRincianBelanjaService) {
        this.subKegiatanRincianBelanjaService = subKegiatanRincianBelanjaService;
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<SubKegiatanRincianBelanja> findAll() {
        return subKegiatanRincianBelanjaService.findAll();
    }

    /**
     * Ambil data berdasarkan kodeSubKegiatan
     * @param kodeSubKegiatan
     */
    @GetMapping("detail/{kodeSubKegiatan}")
    public SubKegiatanRincianBelanja getByKodeSubKegiatan(@PathVariable("kodeSubKegiatan") String kodeSubKegiatan) {
        return subKegiatanRincianBelanjaService.detailSubKegiatanRincianBelanjaByKodeSubKegiatan(kodeSubKegiatan);
    }

    /**
     * Ambil data berdasarkan idRincianBelanja dan kodeSubKegiatan
     * @param idRincianBelanja
     * @param kodeSubKegiatan
     */
    @GetMapping("rincianbelanja/{idRincianBelanja}/subkegiatan/{kodeSubKegiatan}")
    public SubKegiatanRincianBelanja getByIdRincianBelanjaAndKodeSubKegiatan(
            @PathVariable("idRincianBelanja") Integer idRincianBelanja,
            @PathVariable("kodeSubKegiatan") String kodeSubKegiatan) {
        return subKegiatanRincianBelanjaService.findByIdRincianBelanjaAndKodeSubKegiatan(idRincianBelanja, kodeSubKegiatan);
    }

    /**
     * Tambah data berdasarkan idRincianBelanja
     * @param idRincianBelanja
     * @param request
     */
    @PostMapping("rincianbelanja/{idRincianBelanja}")
    public ResponseEntity<SubKegiatanRincianBelanja> post(
            @PathVariable("idRincianBelanja") Integer idRincianBelanja,
            @Valid @RequestBody SubKegiatanRincianBelanjaRequest request) {
        SubKegiatanRincianBelanja saved = subKegiatanRincianBelanjaService.tambahSubKegiatanRincianBelanja(request, idRincianBelanja);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{kodeSubKegiatan}")
                .buildAndExpand(saved.kodeSubKegiatanRincianBelanja())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Ubah data berdasarkan kodeSubKegiatan
     * @param kodeSubKegiatan
     * @param request
     */
    @PutMapping("update/{kodeSubKegiatan}")
    public SubKegiatanRincianBelanja put(
            @PathVariable("kodeSubKegiatan") String kodeSubKegiatan,
            @Valid @RequestBody SubKegiatanRincianBelanjaRequest request) {
        return subKegiatanRincianBelanjaService.ubahSubKegiatanRincianBelanja(kodeSubKegiatan, request);
    }

    /**
     * Hapus berdasarkan kodeSubKegiatan
     * @param kodeSubKegiatan
     */
    @DeleteMapping("delete/{kodeSubKegiatan}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeSubKegiatan") String kodeSubKegiatan) {
        subKegiatanRincianBelanjaService.hapusSubKegiatanRincianBelanja(kodeSubKegiatan);
    }
}