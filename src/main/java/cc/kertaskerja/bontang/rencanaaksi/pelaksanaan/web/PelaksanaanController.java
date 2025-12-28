package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.web;

import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain.Pelaksanaan;
import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain.PelaksanaanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "pelaksanaan rencana aksi")
@RequestMapping("pelaksanaan-renaksi")
public class PelaksanaanController {
    private final PelaksanaanService pelaksanaanService;

    public PelaksanaanController(PelaksanaanService pelaksanaanService) {
        this.pelaksanaanService = pelaksanaanService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public Pelaksanaan getById(@PathVariable("id") Long id) {
        return pelaksanaanService.detailPelaksanaanById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<Pelaksanaan> findAll() {
        return pelaksanaanService.findAll();
    }

    /**
     * Ambil data berdasarkan rencana aksi
     */
    @GetMapping("detail/rencanaaksi/{idRencanaAksi}")
    public Iterable<Pelaksanaan> findByRencanaAksi(@PathVariable("idRencanaAksi") Integer idRencanaAksi) {
        return pelaksanaanService.findByIdRencanaAksiOrderByBulan(idRencanaAksi);
    }

    /**
     * Ubah data berdasarkan id rencana aksi dan id pelaksanaan
     * @param idRencanaAksi
     * @param idPelaksanaan
     */
    @PutMapping("rencanaaksi/{idRencanaAksi}/update/{id}")
    public Pelaksanaan put(@PathVariable("idRencanaAksi") Integer idRencanaAksi, @PathVariable("id") Long id, @Valid @RequestBody PelaksanaanRequest request) {
        return pelaksanaanService.ubahPelaksanaanById(id, request);
    }

    /**
     * Tambah data
     * @param idRencanaAksi
     * @param request
     */
    @PostMapping("{idRencanaAksi}")
    public Pelaksanaan post(@PathVariable("idRencanaAksi") Integer idRencanaAksi, @Valid @RequestBody PelaksanaanRequest request) {
        return pelaksanaanService.tambahPelaksanaan(idRencanaAksi, request);
    }

    /**
     * Hapus berdasarkan id
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        pelaksanaanService.hapusPelaksanaan(id);
    }
}
