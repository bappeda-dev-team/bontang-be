package cc.kertaskerja.bontang.subkegiatanrencanakinerja.web;

import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatan;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatanService;
import cc.kertaskerja.bontang.subkegiatanopd.web.SubKegiatanOpdResponse;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerjaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "subkegiatan rencana kinerja")
@RequestMapping("subkegiatanrencanakinerja")
public class SubKegiatanRencanaKinerjaController {
    private final SubKegiatanRencanaKinerjaService subKegiatanRencanaKinerjaService;
    private final SubKegiatanService subKegiatanService;

    public SubKegiatanRencanaKinerjaController(SubKegiatanRencanaKinerjaService subKegiatanRencanaKinerjaService,
                                               SubKegiatanService subKegiatanService) {
        this.subKegiatanRencanaKinerjaService = subKegiatanRencanaKinerjaService;
        this.subKegiatanService = subKegiatanService;
    }

    /**
     * Ambil semua data
     */
    @GetMapping("detail/findall")
    public Iterable<SubKegiatanRencanaKinerja> findAll() {
        return subKegiatanRencanaKinerjaService.findAll();
    }

    /**
     * Ambil semua data berdasarkan idRencanaKinerja
     * @param idRencanaKinerja
     */
    @GetMapping("detail/{idRencanaKinerja}")
    public List<SubKegiatanRencanaKinerja> getByIdRencanaKinerja(@PathVariable("idRencanaKinerja") Long idRencanaKinerja) {
        return subKegiatanRencanaKinerjaService.findByIdRekin(idRencanaKinerja.intValue());
    }

    @GetMapping("detail/opd/{kodeOpd}")
    public List<SubKegiatanOpdResponse> findByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        List<SubKegiatan> subKegiatans = subKegiatanService.findSubKegiatansForKodeOpd(kodeOpd);
        return subKegiatans.stream()
                .map(this::mapFromSubKegiatan)
                .toList();
    }

    /**
     * Ambil data berdasarkan idRencanaKinerja dan idSubKegiatanRencanaKinerja
     * @param idRencanaKinerja
     * @param idSubKegiatanRencanaKinerja
     */
    @GetMapping("rencanakinerja/{idRencanaKinerja}/detail/{idSubKegiatanRencanaKinerja}")
    public SubKegiatanRencanaKinerjaResponse getByIdRencanaKinerjaAndId(@PathVariable("idRencanaKinerja") Long idRencanaKinerja,
                                                                          @PathVariable("idSubKegiatanRencanaKinerja") Long idSubKegiatanRencanaKinerja) {
        SubKegiatanRencanaKinerja data = subKegiatanRencanaKinerjaService.findByIdRekinAndId(idRencanaKinerja.intValue(), idSubKegiatanRencanaKinerja);
        return SubKegiatanRencanaKinerjaResponse.from(data);
    }

    /**
     * Tambah data
     * @param request
     * @param idRencanaKinerja
     */
    @PostMapping("{idRencanaKinerja}")
    public ResponseEntity<SubKegiatanRencanaKinerjaResponse> post(@PathVariable("idRencanaKinerja") Long idRencanaKinerja, @Valid @RequestBody SubKegiatanRencanaKinerjaRequest request) {
        SubKegiatanRencanaKinerja saved = subKegiatanRencanaKinerjaService.tambahSubKegiatanRencanaKinerja(request, idRencanaKinerja.intValue());
        SubKegiatanRencanaKinerjaResponse response = SubKegiatanRencanaKinerjaResponse.from(saved);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Hapus berdasarkan idSubKegiatanRencanaKinerja
     * @param idSubKegiatanRencanaKinerja
     */
    @DeleteMapping("delete/{idSubKegiatanRencanaKinerja}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("idSubKegiatanRencanaKinerja") Long idSubKegiatanRencanaKinerja) {
        subKegiatanRencanaKinerjaService.hapusSubKegiatanRencanaKinerja(idSubKegiatanRencanaKinerja);
    }

    private SubKegiatanOpdResponse mapFromSubKegiatan(SubKegiatan subKegiatan) {
        return new SubKegiatanOpdResponse(
                subKegiatan.id(),
                subKegiatan.kodeSubKegiatan(),
                subKegiatan.namaSubKegiatan(),
                subKegiatan.kodeOpd(),
                subKegiatan.tahun(),
                subKegiatan.createdDate(),
                subKegiatan.lastModifiedDate()
        );
    }
}
