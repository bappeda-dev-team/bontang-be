package cc.kertaskerja.bontang.target.web;

import cc.kertaskerja.bontang.target.domain.Target;
import cc.kertaskerja.bontang.target.domain.TargetNotFoundException;
import cc.kertaskerja.bontang.target.domain.TargetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "target")
@RequestMapping("target")
public class TargetController {
    private final TargetService targetService;

    public TargetController(TargetService targetService) {
        this.targetService = targetService;
    }

    /**
     * Ambil data berdasarkan id
     * @param id
     */
    @GetMapping("detail/{id}")
    public Target getById(@PathVariable("id") Long id) {
        return targetService.detailTargetById(id);
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<Target> findAll() {
        return targetService.findAllTargets();
    }

    /**
     * Ubah data berdasarkan id
     * @param id
     */
    @PutMapping("update/{id}")
    public Target put(@PathVariable("id") Long id, @Valid @RequestBody TargetRequest request) {
        Target existingTarget = targetService.detailTargetById(id);

        Target target = new Target(
                existingTarget.id(),
                request.target(),
                request.satuan(),
                existingTarget.createdDate(),
                null
        );

        return targetService.ubahTarget(id, target);
    }

    /**
     * Tambah data
     * @param request
     */
    @PostMapping
    public ResponseEntity<Target> post(@Valid @RequestBody TargetRequest request) {
        Target target = Target.of(
                request.target(),
                request.satuan()
        );
        Target saved = targetService.tambahTarget(target);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus berdasarkan id
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        targetService.hapusTarget(id);
    }
}