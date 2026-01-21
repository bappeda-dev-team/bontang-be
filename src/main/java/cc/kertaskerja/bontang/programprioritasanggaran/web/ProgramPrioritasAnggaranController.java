package cc.kertaskerja.bontang.programprioritasanggaran.web;

import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaran;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRencanaKinerja;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranService;
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

    @PostMapping
    public ResponseEntity<ProgramPrioritasAnggaran> create(@Valid @RequestBody ProgramPrioritasAnggaranRequest request) {
        ProgramPrioritasAnggaran saved = programPrioritasAnggaranService.simpanPerencanaanAnggaran(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
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

    @DeleteMapping("delete/{id}/rencana-kinerja/{idRencanaKinerja}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRencanaKinerja(
            @PathVariable("id") Long id,
            @PathVariable("idRk") Long idRencanaKinerja
    ) {
        programPrioritasAnggaranService.removeRencanaKinerja(id, idRencanaKinerja);
    }
}
