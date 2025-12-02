package cc.kertaskerja.bontang.program.web;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.program.domain.ProgramService;
import cc.kertaskerja.bontang.program.web.request.ProgramBatchRequest;
import cc.kertaskerja.bontang.program.web.request.ProgramRequest;
import cc.kertaskerja.bontang.program.web.response.ProgramResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@Tag(name = "program")
@RequestMapping("program")
public class ProgramController {
    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    /**
     * Ambil data berdasarkan kode program
     * @param kodeProgram
     */
    @GetMapping("detail/{kodeProgram}")
    public Program getByKodeProgram(@PathVariable("kodeProgram") String kodeProgram) {
        return programService.detailProgramByKodeProgram(kodeProgram);
    }

    /**
     * Ambil semua data program
     */
    @GetMapping("detail/findall")
    public List<ProgramResponse> findAll() {
        Iterable<Program> programs = programService.findAll();
        return StreamSupport.stream(programs.spliterator(), false)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Ambil semua data program berdasarkan kode opd
     * @param kodeOpd
     */
    @GetMapping("detail/findall/{kodeOpd}")
    public List<ProgramResponse> findAllByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        Iterable<Program> programs = programService.findAllByKodeOpd(kodeOpd);
        return StreamSupport.stream(programs.spliterator(), false)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Ubah data program berdasarkan kode program
     * @param kodeProgram
     */
    @PutMapping("update/{kodeProgram}")
    public Program put(@PathVariable("kodeProgram") String kodeProgram, @Valid @RequestBody ProgramRequest request) {
        Program existingProgram = programService.detailProgramByKodeProgram(kodeProgram);

        Program program = new Program(
                existingProgram.id(),
                request.kodeProgram(),
                request.namaProgram(),
                existingProgram.bidangUrusanId(),
                existingProgram.createdDate(),
                null
        );

        return programService.ubahProgram(kodeProgram, program, request.kodeBidangUrusan());
    }

    /**
     * Tambah data program
     * @param request
     */
    @PostMapping
    public ResponseEntity<Program> post(@Valid @RequestBody ProgramRequest request) {
        Program program = Program.of(
                request.kodeProgram(),
                request.namaProgram(),
                null
        );
        Program saved = programService.tambahProgram(program, request.kodeBidangUrusan());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Ambil data program berdasarkan kumpulan kode program
     */
    @PostMapping("{kodeOpd}/find/batch")
    public List<Program> findBatch(
            @PathVariable("kodeOpd") String kodeOpd,
            @Valid @RequestBody ProgramBatchRequest request
    ) {
        return programService.detailProgramByKodeProgramInAndKodeOpd(request.kodeProgram(), kodeOpd);
    }

    /**
     * Hapus program berdasarkan kode program
     * @param kodeProgram
     */
    @DeleteMapping("delete/{kodeProgram}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeProgram") String kodeProgram) {
        programService.hapusProgram(kodeProgram);
    }

    /**
     * Hapus program berdasarkan kode opd dan kode program
     * @param kodeOpd
     * @param kodeProgram
     */
    @DeleteMapping("delete/{kodeOpd}/{kodeProgram}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByKodeOpdAndKodeProgram(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("kodeProgram") String kodeProgram
    ) {
        programService.hapusProgram(kodeOpd, kodeProgram);
    }

    private ProgramResponse mapToResponse(Program program) {
        String kodeBidangUrusan = programService.getKodeBidangUrusan(program.bidangUrusanId());
        return new ProgramResponse(
                program.id(),
                program.kodeProgram(),
                program.namaProgram(),
                kodeBidangUrusan,
                program.createdDate(),
                program.lastModifiedDate()
        );
    }
}
