package cc.kertaskerja.bontang.pegawai.web;

import cc.kertaskerja.bontang.pegawai.domain.Pegawai;
import cc.kertaskerja.bontang.pegawai.domain.PegawaiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Tag(name = "pegawai")
@RequestMapping("pegawai")
public class PegawaiController {
    private final PegawaiService pegawaiService;

    public PegawaiController(PegawaiService pegawaiService) {
        this.pegawaiService = pegawaiService;
    }

    /**
     * Ambil data berdasarkan id
     * @param nip
     */
    @GetMapping("detail/{nip}")
    public Pegawai getByNip(@PathVariable("nip") String nip) {
        return pegawaiService.detailPegawaiByNip(nip);
    }

    /**
     * Ambil semua data pegawai
     */
    @GetMapping("detail/findall")
    public Iterable<Pegawai> findAll() {
        return pegawaiService.findAll();
    }

    /**
     * Ubah data pegawai berdasarkan nip
     * @param nip
     */
    @PutMapping("update/{nip}")
    public Pegawai put(@PathVariable("nip") String nip, @Valid @RequestBody PegawaiRequest request) {
        Pegawai existingPegawai = pegawaiService.detailPegawaiByNip(nip);

        Pegawai pegawai = new Pegawai(
                existingPegawai.id(),
                null,
                request.namaPegawai(),
                request.nip(),
                request.email(),
                request.jabatanDinas(),
                request.jabatanTim(),
                existingPegawai.createdDate(),
                null
        );

        return pegawaiService.ubahPegawai(nip, pegawai, request.kodeOpd());
    }

    /**
     * Tambah data pegawai
     * @param request
     */
    @PostMapping
    public ResponseEntity<Pegawai> post(@Valid @RequestBody PegawaiRequest request) {
        Pegawai pegawai = Pegawai.of(
                request.namaPegawai(),
                request.nip(),
                request.email(),
                request.jabatanDinas(),
                request.jabatanTim(),
                null
        );
        Pegawai saved = pegawaiService.tambahPegawai(pegawai, request.kodeOpd());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus pegawai berdasarkan nip pegawai
     * @param nip
     */
    @DeleteMapping("delete/{nip}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("nip") String nip) {
        pegawaiService.hapusPegawai(nip);
    }
}
