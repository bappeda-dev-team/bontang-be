package cc.kertaskerja.bontang.rencanaaksi.web;

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

import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@Tag(name = "rencana aksi")
@RequestMapping("rencanaaksi")
public class RencanaAksiController {
    private final RencanaAksiService rencanaAksiService;

    public RencanaAksiController(RencanaAksiService rencanaAksiService) {
        this.rencanaAksiService = rencanaAksiService;
    }

    // Ambil data rencana aksi by id
    @GetMapping("detail/{id}")
    public RencanaAksi getById(@PathVariable("id") Long id) {
        return rencanaAksiService.detailRencanaAksiById(id);
    }

    // Ambil semua data rencana aksi
    @GetMapping("detail/findall")
    public Iterable<RencanaAksi> findAll() {
        return rencanaAksiService.findAll();
    }

    // Ambil data rencana aksi by rencana kinerja
    @GetMapping("detail/rencanakinerja/{idRencanaKinerja}")
    public Iterable<RencanaAksi> findByRencanaKinerja(@PathVariable("idRencanaKinerja") Integer idRencanaKinerja) {
        return rencanaAksiService.findByIdRekinOrderByUrutan(idRencanaKinerja);
    }

    // Ubah by id
    @PutMapping("update/{id}")
    public RencanaAksi put(@PathVariable("id") Long id, @Valid @RequestBody RencanaAksiRequest request) {
        RencanaAksi existingRencanaAksi = rencanaAksiService.detailRencanaAksiById(id);

        RencanaAksi rencanaAksi = new RencanaAksi(
                existingRencanaAksi.id(),
                existingRencanaAksi.idRekin(),
                request.urutan(),
                request.namaRencanaAksi(),
                existingRencanaAksi.createdDate(),
                null
        );

        return rencanaAksiService.ubahRencanaAksi(id, rencanaAksi);
    }
    
    // Tambah by idRencanaKinerja
    @PostMapping("{idRencanaKinerja}")
    public ResponseEntity<RencanaAksi> post(@PathVariable("idRencanaKinerja") Integer idRencanaKinerja,
                                             @Valid @RequestBody RencanaAksiRequest request) {
        RencanaAksi rencanaAksi = RencanaAksi.of(
                idRencanaKinerja,
                request.urutan(),
                request.namaRencanaAksi()
        );
        RencanaAksi saved = rencanaAksiService.tambahRencanaAksi(rencanaAksi);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // Hapus by id
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        rencanaAksiService.hapusRencanaAksi(id);
    }
}
