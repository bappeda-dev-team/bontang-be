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
    @GetMapping("detail/get-all-programprioritas")
    public Iterable<ProgramPrioritas> findAll(
            @RequestParam(value = "tahun_awal", required = false) Integer periodeTahunAwal,
            @RequestParam(value = "tahun_akhir", required = false) Integer periodeTahunAkhir
    ) {
        if (periodeTahunAwal != null && periodeTahunAkhir != null) {
            return programPrioritasService.findByPeriodeTahunRange(periodeTahunAwal, periodeTahunAkhir);
        }
        return programPrioritasService.findAll();
    }

    /**
     * Ubah data program prioritas berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public ProgramPrioritas put(@PathVariable("id") Long id, @Valid @RequestBody ProgramPrioritasRequest request) {
        return programPrioritasService.ubahProgramPrioritas(id, request);
    }

    /**
     * Tambah data program prioritas
     * @param request
     */
    @PostMapping
    public ResponseEntity<ProgramPrioritas> post(@Valid @RequestBody ProgramPrioritasRequest request) {
        ProgramPrioritas saved = programPrioritasService.tambahProgramPrioritas(request);
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
