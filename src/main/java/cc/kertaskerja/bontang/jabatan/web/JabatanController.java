package cc.kertaskerja.bontang.jabatan.web;

import cc.kertaskerja.bontang.jabatan.domain.Jabatan;
import cc.kertaskerja.bontang.jabatan.domain.JabatanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import java.net.URI;

@RestController
@Tag(name = "jabatan")
@RequestMapping("jabatan")
public class JabatanController {
    private final JabatanService jabatanService;

    public JabatanController(JabatanService jabatanService) {
        this.jabatanService = jabatanService;
    }

    @GetMapping("detail/{kodeJabatan}")
    public Jabatan detail(@PathVariable("kodeJabatan") String kodeJabatan) {
        return jabatanService.detailJabatanByKodeJabatan(kodeJabatan);
    }

    @GetMapping("opd/{kodeOpd}")
    public Iterable<Jabatan> findByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        return jabatanService.findByKodeOpd(kodeOpd);
    }

    @PostMapping
    public ResponseEntity<Jabatan> post(@Valid @RequestBody JabatanControllerRequest request) {
        Jabatan saved = jabatanService.tambahJabatan(
                Jabatan.of(
                        request.namaJabatan(),
                        request.kodeJabatan(),
                        request.jenisJabatan(),
                        request.kodeOpd()
                )
        );
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("update/{kodeJabatan}")
    public Jabatan put(
            @PathVariable("kodeJabatan") String kodeJabatan,
            @Valid @RequestBody JabatanControllerRequest request
    ) {
        Jabatan payload = Jabatan.of(
                request.namaJabatan(),
                request.kodeJabatan(),
                request.jenisJabatan(),
                request.kodeOpd()
        );
        return jabatanService.ubahJabatan(kodeJabatan, payload);
    }

    @DeleteMapping("delete/{kodeJabatan}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeJabatan") String kodeJabatan) {
        jabatanService.hapusJabatan(kodeJabatan);
    }
}
