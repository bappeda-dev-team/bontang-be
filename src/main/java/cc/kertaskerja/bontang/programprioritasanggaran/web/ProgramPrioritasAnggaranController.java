package cc.kertaskerja.bontang.programprioritasanggaran.web;

import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaran;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRencanaKinerja;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "program prioritas anggaran")
@RequestMapping("programprioritasanggaran")
public class ProgramPrioritasAnggaranController {
    private final ProgramPrioritasAnggaranService programPrioritasAnggaranService;

    public ProgramPrioritasAnggaranController(ProgramPrioritasAnggaranService programPrioritasAnggaranService) {
        this.programPrioritasAnggaranService = programPrioritasAnggaranService;
    }

    @GetMapping("detail/{id}")
    public ProgramPrioritasAnggaran getById(@PathVariable("id") Long id) {
        return programPrioritasAnggaranService.detailProgramPrioritasAnggaranById(id);
    }

    @GetMapping("detail/get-all-programprioritasanggarans")
    public Iterable<ProgramPrioritasAnggaran> findAll() {
        return programPrioritasAnggaranService.findAll();
    }

    @GetMapping("detail/kode-opd/{kodeOpd}")
    public Iterable<ProgramPrioritasAnggaran> getByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        return programPrioritasAnggaranService.getByKodeOpd(kodeOpd);
    }

    @GetMapping("detail/kode-opd/{kodeOpd}/nip/{nip}/tahun/{tahun}")
    public List<ProgramPrioritasAnggaranWithRencanaKinerjaResponse> getByKodeOpdNipTahun(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("nip") String nip,
            @PathVariable("tahun") Integer tahun
    ) {
        return programPrioritasAnggaranService.getByKodeOpdNipTahun(kodeOpd, nip, tahun);
    }

    @PostMapping
    public ResponseEntity<List<ProgramPrioritasAnggaran>> create(
            @Valid @RequestBody ProgramPrioritasAnggaranBulkCreateRequest request
    ) {
        List<ProgramPrioritasAnggaran> saved = programPrioritasAnggaranService
                .simpanPerencanaanAnggaran(request.programPrioritas());

        if (saved.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.get(0).id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("update/{id}")
    public ProgramPrioritasAnggaran update(@PathVariable("id") Long id, @Valid @RequestBody ProgramPrioritasAnggaranRequest request) {
        return programPrioritasAnggaranService.ubahPerencanaanAnggaran(id, request);
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        programPrioritasAnggaranService.hapusProgramPrioritasAnggaran(id);
    }

    @GetMapping("{id}/rencana-kinerja")
    public Iterable<ProgramPrioritasAnggaranRencanaKinerja> getListRencanaKinerja(@PathVariable("id") Long id) {
        return programPrioritasAnggaranService.getListRencanaKinerja(id);
    }

    @PostMapping("{id}/rencana-kinerja")
    public List<ProgramPrioritasAnggaranRencanaKinerjaResponse> addRencanaKinerja(
            @PathVariable("id") Long id,
            @Valid @RequestBody RencanaKinerjaBatchRequest request
    ) {
        return programPrioritasAnggaranService.addRencanaKinerjaBatch(id, request.getRencanaKinerja());
    }

    /**
     * Cek relasi antara program prioritas dengan program prioritas anggaran
     */
    @Operation(summary = "Cek relasi program prioritas dengan program prioritas anggaran",
            description = "Memeriksa apakah program prioritas memiliki relasi dengan program prioritas anggaran dan menghitung jumlah relasinya")
    @GetMapping("check-relasi-program-prioritas/{id}")
    public CheckRelasiProgramPrioritasResponse checkRelasiProgramPrioritas(@PathVariable("id") Long id) {
        return programPrioritasAnggaranService.checkRelasiByProgramPrioritas(id);
    }

    /**
     * Cek relasi antara rencana kinerja dengan program prioritas anggaran
     */
    @Operation(summary = "Cek relasi rencana kinerja dengan program prioritas anggaran",
            description = "Memeriksa apakah rencana kinerja memiliki relasi dengan program prioritas anggaran dan menghitung jumlah relasinya")
    @GetMapping("check-relasi-rencana-kinerja/{id}")
    public CheckRelasiRencanaKinerjaResponse checkRelasiRencanaKinerja(@PathVariable("id") Long id) {
        return programPrioritasAnggaranService.checkRelasiByRencanaKinerja(id);
    }
}
