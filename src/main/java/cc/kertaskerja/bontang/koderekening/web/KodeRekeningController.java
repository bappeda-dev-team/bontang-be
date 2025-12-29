package cc.kertaskerja.bontang.koderekening.web;

import cc.kertaskerja.bontang.koderekening.domain.KodeRekening;
import cc.kertaskerja.bontang.koderekening.domain.KodeRekeningService;
import cc.kertaskerja.bontang.program.domain.Program;
import cc.kertaskerja.bontang.program.domain.ProgramService;
import cc.kertaskerja.bontang.program.web.request.ProgramRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "kode rekening")
@RequestMapping("koderekening")
public class KodeRekeningController {
    private final KodeRekeningService kodeRekeningService;

    public KodeRekeningController(KodeRekeningService kodeRekeningService) {
        this.kodeRekeningService = kodeRekeningService;
    }

    /**
     * Ambil data berdasarkan kode rekening service
     * @param kodeRekening
     */
    @GetMapping("detail/{kodeRekening}")
    public KodeRekening getByKodeRekening(@PathVariable("kodeRekening") String kodeRekening) {
        return kodeRekeningService.detailKodeRekeningByKodeRekening(kodeRekening);
    }

    /**
     * Ambil semua data kode rekening
     */
    @GetMapping("detail/get-all-koderekenings")
    public Iterable<KodeRekening> findAll() {
        return kodeRekeningService.findAll();
    }

    /**
     * Ubah data kode rekening berdasarkan kode rekening
     * @param kodeRekening
     */
    @PutMapping("update/{kodeRekening}")
    public KodeRekening put(@PathVariable("kodeRekening") String kodeRekening, @Valid @RequestBody KodeRekeningRequest request) {
        KodeRekening existingKodeRekening = kodeRekeningService.detailKodeRekeningByKodeRekening(kodeRekening);

        KodeRekening KodeRekening = new KodeRekening(
                existingKodeRekening.id(),
                request.kodeRekening(),
                request.namaRekening(),
                existingKodeRekening.createdDate(),
                null
        );

        return kodeRekeningService.ubahKodeRekening(kodeRekening, KodeRekening);
    }

    /**
     * Tambah data kode rekening
     * @param request
     */
    @PostMapping
    public ResponseEntity<KodeRekening> post(@Valid @RequestBody KodeRekeningRequest request) {
        KodeRekening kodeRekening = KodeRekening.of(
                request.kodeRekening(),
                request.namaRekening()
        );
        KodeRekening saved = kodeRekeningService.tambahKodeRekening(kodeRekening);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus kode rekening berdasarkan kode rekening
     * @param kodeRekening
     */
    @DeleteMapping("delete/{kodeRekening}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeRekening") String kodeRekening) {
        kodeRekeningService.hapusKodeRekening(kodeRekening);
    }
}
