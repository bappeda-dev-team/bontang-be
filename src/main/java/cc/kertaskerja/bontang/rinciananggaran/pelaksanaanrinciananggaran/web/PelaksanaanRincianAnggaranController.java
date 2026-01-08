package cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.web;

import cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.domain.PelaksanaanRincianAnggaran;
import cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.domain.PelaksanaanRincianAnggaranService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "pelaksanaan rincian anggaran")
@RequestMapping("pelaksanaan-rincian-anggaran")
public class PelaksanaanRincianAnggaranController {
    private final PelaksanaanRincianAnggaranService pelaksanaanRincianAnggaranService;

    public PelaksanaanRincianAnggaranController(PelaksanaanRincianAnggaranService pelaksanaanRincianAnggaranService) {
        this.pelaksanaanRincianAnggaranService = pelaksanaanRincianAnggaranService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public PelaksanaanRincianAnggaran getById(@PathVariable("id") Long id) {
        return pelaksanaanRincianAnggaranService.detailPelaksanaanRincianAnggaranById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<PelaksanaanRincianAnggaran> findAll() {
        return pelaksanaanRincianAnggaranService.findAll();
    }

    /**
     * Ambil data berdasarkan rincian anggaran
     */
    @GetMapping("detail/rinciananggaran/{idRincianAnggaran}")
    public Iterable<PelaksanaanRincianAnggaran> findByRincianAnggaran(@PathVariable("idRincianAnggaran") Integer idRincianAnggaran) {
        return pelaksanaanRincianAnggaranService.findByIdRincianAnggaranOrderByBulan(idRincianAnggaran);
    }

    /**
     * Ubah data berdasarkan id rincian anggaran dan id pelaksanaan
     * @param idRincianAnggaran
     * @param idPelaksanaanRincianAnggaran
     */
    @PutMapping("rinciananggaran/{idRincianAnggaran}/update/{id}")
    public PelaksanaanRincianAnggaran put(@PathVariable("idRincianAnggaran") String idRincianAnggaran, @PathVariable("id") Long id, @Valid @RequestBody PelaksanaanRincianAnggaranRequest request) {
        return pelaksanaanRincianAnggaranService.ubahPelaksanaanRincianAnggaranById(id, request);
    }

    /**
     * Tambah data
     * @param idRincianAnggaran
     * @param request
     */
    @PostMapping("{idRincianAnggaran}")
    public PelaksanaanRincianAnggaran post(@PathVariable("idRincianAnggaran") Integer idRincianAnggaran, @Valid @RequestBody PelaksanaanRincianAnggaranRequest request) {
        return pelaksanaanRincianAnggaranService.tambahPelaksanaanRincianAnggaran(idRincianAnggaran, request);
    }

    /**
     * Hapus berdasarkan id
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        pelaksanaanRincianAnggaranService.hapusPelaksanaanRincianAnggaran(id);
    }
}