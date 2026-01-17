package cc.kertaskerja.bontang.programprioritasopd.web;

import cc.kertaskerja.bontang.programprioritasopd.domain.ProgramPrioritasOpd;
import cc.kertaskerja.bontang.programprioritasopd.domain.ProgramPrioritasOpdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "program prioritas opd")
@RequestMapping("programprioritasopd")
public class ProgramPrioritasOpdController {
    private final ProgramPrioritasOpdService programPrioritasOpdService;

    public ProgramPrioritasOpdController(ProgramPrioritasOpdService programPrioritasOpdService) {
        this.programPrioritasOpdService = programPrioritasOpdService;
    }

    @GetMapping("detail/{id}")
    public ProgramPrioritasOpd getById(@PathVariable("id") Long id) {
        return programPrioritasOpdService.detailProgramPrioritasOpdById(id);
    }

    @GetMapping("detail/programprioritas/{idProgramPrioritas}")
    public Iterable<ProgramPrioritasOpd> getByIdProgramPrioritas(@PathVariable("idProgramPrioritas") Long idProgramPrioritas) {
        return programPrioritasOpdService.getByIdProgramPrioritas(idProgramPrioritas);
    }

    @GetMapping("detail/subkegiatanopd/{idSubkegiatanOpd}")
    public Iterable<ProgramPrioritasOpd> getByIdSubkegiatanOpd(@PathVariable("idSubkegiatanOpd") Long idSubkegiatanOpd) {
        return programPrioritasOpdService.getByIdSubkegiatanOpd(idSubkegiatanOpd);
    }

    @GetMapping("detail/rencanakinerja/{idRencanaKinerja}")
    public Iterable<ProgramPrioritasOpd> getByIdRencanaKinerja(@PathVariable("idRencanaKinerja") Long idRencanaKinerja) {
        return programPrioritasOpdService.getByIdRencanaKinerja(idRencanaKinerja);
    }

    @GetMapping("detail/get-all-programprioritasopd")
    public Iterable<ProgramPrioritasOpd> findAll() {
        return programPrioritasOpdService.findAll();
    }

    @PutMapping("update/{id}")
    public ProgramPrioritasOpd put(@PathVariable("id") Long id, @Valid @RequestBody ProgramPrioritasOpdRequest request) {
        return programPrioritasOpdService.ubahProgramPrioritasOpd(id, request);
    }

    @PostMapping
    public ResponseEntity<ProgramPrioritasOpd> post(@Valid @RequestBody ProgramPrioritasOpdRequest request) {
        ProgramPrioritasOpd saved = programPrioritasOpdService.tambahProgramPrioritasOpd(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        programPrioritasOpdService.hapusProgramPrioritasOpd(id);
    }
}
