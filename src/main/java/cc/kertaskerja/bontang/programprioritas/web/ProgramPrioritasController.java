package cc.kertaskerja.bontang.programprioritas.web;

import cc.kertaskerja.bontang.programprioritas.domain.ProgramPrioritas;
import cc.kertaskerja.bontang.programprioritas.domain.ProgramPrioritasService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "program prioritas")
@RequestMapping("programprioritas")
public class ProgramPrioritasController {
    private final ProgramPrioritasService programPrioritasService;

    public ProgramPrioritasController(ProgramPrioritasService programPrioritasService) {
        this.programPrioritasService = programPrioritasService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public ProgramPrioritas getById(@PathVariable("id") Long id) {
        return programPrioritasService.detailProgramPrioritasById(id);
    }

    /**
     * Ambil semua data program prioritas
     */
    @GetMapping("detail/findall")
    public Iterable<ProgramPrioritas> findAll() {
        return programPrioritasService.findAll();
    }

    /**
     * Ubah data program prioritas berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public ProgramPrioritas put(@PathVariable("id") Long id, @Valid @RequestBody ProgramPrioritasRequest request) {
        ProgramPrioritas existingProgramPrioritas = programPrioritasService.detailProgramPrioritasById(id);

        ProgramPrioritas programPrioritas = new ProgramPrioritas(
                existingProgramPrioritas.id(),
                request.programPrioritas(),
                existingProgramPrioritas.createdDate(),
                null
        );

        return programPrioritasService.ubahProgramPrioritas(id, programPrioritas);
    }

    /**
     * Tambah data program prioritas
     * @param request
     */
    @PostMapping
    public ResponseEntity<ProgramPrioritas> post(@Valid @RequestBody ProgramPrioritasRequest request) {
        ProgramPrioritas programPrioritas = ProgramPrioritas.of(
                request.programPrioritas()
        );
        ProgramPrioritas saved = programPrioritasService.tambahProgramPrioritas(programPrioritas);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus program prioritas berdasarkan id
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        programPrioritasService.hapusProgramPrioritas(id);
    }
}
