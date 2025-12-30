package cc.kertaskerja.bontang.programopd.web;

import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.program.domain.ProgramService;
import cc.kertaskerja.bontang.program.web.request.ProgramBatchRequest;
import cc.kertaskerja.bontang.programopd.domain.ProgramOpd;
import cc.kertaskerja.bontang.programopd.domain.ProgramOpdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@Tag(name = "program opd")
@RequestMapping("programopd")
public class ProgramOpdController {
    private final ProgramOpdService programOpdService;
    private final ProgramService programService;

    public ProgramOpdController(ProgramOpdService programOpdService, ProgramService programService) {
        this.programOpdService = programOpdService;
        this.programService = programService;
    }

    // get program opd by kode program opd
    @GetMapping("detail/{kodeProgramOpd}")
    public ProgramOpd getByKodeProgramOpd(@PathVariable("kodeProgramOpd") String kodeProgramOpd) {
        return programOpdService.detailProgramOpdByKodeProgram(kodeProgramOpd);
    }

    // get program opd by kode opd and tahun
    @GetMapping("detail/kodeOpd/{kodeOpd}/tahun/{tahun}")
    public List<ProgramOpdResponse> findByKodeOpdAndTahun(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun) {
        Iterable<ProgramOpd> programOpds = programOpdService.findByKodeOpdAndTahun(kodeOpd, tahun);
        return StreamSupport.stream(programOpds.spliterator(), false)
                .map(this::map)
                .toList();
    }

    // update program opd by kode program opd
    @PutMapping("update/{kodeProgramOpd}")
    public ProgramOpd put(@PathVariable("kodeProgramOpd") String kodeProgramOpd, @Valid @RequestBody ProgramOpdRequest request) {
        ProgramOpd existingProgramOpd = programOpdService.detailProgramOpdByKodeProgram(kodeProgramOpd);

        ProgramOpd programOpd = new ProgramOpd(
                existingProgramOpd.id(),
                request.kodeProgramOpd(),
                request.namaProgramOpd(),
                request.kodeOpd(),
                request.tahun(),
                existingProgramOpd.createdDate(),
                null
        );

        return programOpdService.ubahProgramOpd(kodeProgramOpd, programOpd);
    }

    // create program opd
    @PostMapping
    public ResponseEntity<ProgramOpd> post(@Valid @RequestBody ProgramOpdRequest request) {
        ProgramOpd programOpd = ProgramOpd.of(
                request.kodeProgramOpd(),
                request.namaProgramOpd(),
                request.kodeOpd(),
                request.tahun()
        );
        ProgramOpd saved = programOpdService.tambahProgramOpd(programOpd);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // ambil data program opd berdasarkan kumpulan kode program
    @PostMapping("/find/batch/kode-program")
    public List<Program> findProgramBatch(@Valid @RequestBody ProgramBatchRequest request) {
        List<Program> programs = programService.detailProgramByKodeProgramIn(request.kodeProgram());
        programs.forEach(programOpdService::simpanProgramOpd);
        return programs;
    }

    // hapus program opd
    @DeleteMapping("delete/{kodeProgramOpd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeProgramOpd") String kodeProgramOpd) {
        programOpdService.hapusProgramOpd(kodeProgramOpd);
    }

    private ProgramOpdResponse map(ProgramOpd programOpd) {
        return new ProgramOpdResponse(
                programOpd.id(),
                programOpd.kodeProgramOpd(),
                programOpd.namaProgramOpd(),
                programOpd.kodeOpd(),
                programOpd.tahun(),
                programOpd.createdDate(),
                programOpd.lastModifiedDate()
        );
    }
}
