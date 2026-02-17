package cc.kertaskerja.bontang.subkegiatanopd.web;

import java.net.URI;
import java.util.List;
import java.util.stream.StreamSupport;

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

import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatan;
import cc.kertaskerja.bontang.subkegiatan.domain.SubKegiatanService;
import cc.kertaskerja.bontang.subkegiatan.web.request.SubKegiatanBatchRequest;
import cc.kertaskerja.bontang.subkegiatanopd.domain.SubKegiatanOpd;
import cc.kertaskerja.bontang.subkegiatanopd.domain.SubKegiatanOpdService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "SubKegiatan Opd")
@RequestMapping("subkegiatanopd")
public class SubKegiatanOpdController {
    private final SubKegiatanOpdService subKegiatanOpdService;
    private final SubKegiatanService subKegiatanService;

    public SubKegiatanOpdController(SubKegiatanOpdService subKegiatanOpdService, SubKegiatanService subKegiatanService) {
        this.subKegiatanOpdService = subKegiatanOpdService;
        this.subKegiatanService = subKegiatanService;
    }

    // get sub kegiatan opd by kode sub kegiatan opd
    @GetMapping("detail/{kodeSubKegiatanOpd}")
    public SubKegiatanOpd getByKodeSubKegiatanOpd(@PathVariable("kodeSubKegiatanOpd") String kodeSubKegiatanOpd) {
        return subKegiatanOpdService.detailSubKegiatanOpdByKodeSubKegiatan(kodeSubKegiatanOpd);
    }

    // get sub kegiatan opd by kode opd dan tahun
    @GetMapping("detail/kodeOpd/{kodeOpd}/tahun/{tahun}")
    public List<SubKegiatanOpdResponse> findByKodeOpdAndTahun(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun) {
        Iterable<SubKegiatanOpd> subKegiatanOpds = subKegiatanOpdService.findByKodeOpdAndTahun(kodeOpd, tahun);
        return StreamSupport.stream(subKegiatanOpds.spliterator(), false)
                .map(this::map)
                .toList();
    }

    @GetMapping("detail/opd/{kodeOpd}")
    public List<SubKegiatanOpdResponse> findByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        List<SubKegiatan> subKegiatans = subKegiatanService.findSubKegiatansForKodeOpd(kodeOpd);
        return subKegiatans.stream()
                .map(this::mapFromSubKegiatan)
                .toList();
    }

    // update sub kegiatan opd by kode sub kegiatan opd
    @PutMapping("update/{kodeSubKegiatanOpd}")
    public SubKegiatanOpd put(@PathVariable("kodeSubKegiatanOpd") String kodeSubKegiatanOpd, @Valid @RequestBody SubKegiatanOpdRequest request) {
        SubKegiatanOpd existingSubKegiatanOpd = subKegiatanOpdService.detailSubKegiatanOpdByKodeSubKegiatan(kodeSubKegiatanOpd);

        SubKegiatanOpd subKegiatanOpd = new SubKegiatanOpd(
                existingSubKegiatanOpd.id(),
                request.kodeSubKegiatanOpd(),
                request.namaSubKegiatanOpd(),
                request.kodeOpd(),
                request.tahun(),
                existingSubKegiatanOpd.createdDate(),
                null
        );

        return subKegiatanOpdService.ubahSubKegiatanOpd(kodeSubKegiatanOpd, subKegiatanOpd);
    }

    // create sub kegiatan opd
    @PostMapping
    public ResponseEntity<SubKegiatanOpd> post(@Valid @RequestBody SubKegiatanOpdRequest request) {
        SubKegiatanOpd subKegiatanOpd = SubKegiatanOpd.of(
                request.kodeSubKegiatanOpd(),
                request.namaSubKegiatanOpd(),
                request.kodeOpd(),
                request.tahun()
        );
        SubKegiatanOpd saved = subKegiatanOpdService.tambahSubKegiatanOpd(subKegiatanOpd);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // ambil data sub kegiatan opd berdasarkan kumpulan kode sub kegiatan
    @PostMapping("/find/batch/kode-subkegiatan")
    public List<SubKegiatan> findSubKegiatanBatch(@Valid @RequestBody SubKegiatanBatchRequest request) {
        List<SubKegiatan> subKegiatans = subKegiatanService.detailSubKegiatanIn(request.kodeSubKegiatan());
        subKegiatans.forEach(subKegiatanOpdService::simpanSubKegiatanOpd);
        return subKegiatans;
    }

    // hapus sub kegiatan opd
    @DeleteMapping("delete/{kodeSubKegiatanOpd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeSubKegiatanOpd") String kodeSubKegiatanOpd) {
        subKegiatanOpdService.hapusSubKegiatanOpd(kodeSubKegiatanOpd);
    }

    private SubKegiatanOpdResponse map(SubKegiatanOpd subKegiatanOpd) {
        return new SubKegiatanOpdResponse(
                subKegiatanOpd.id(),
                subKegiatanOpd.kodeSubKegiatanOpd(),
                subKegiatanOpd.namaSubKegiatanOpd(),
                subKegiatanOpd.kodeOpd(),
                subKegiatanOpd.tahun(),
                subKegiatanOpd.createdDate(),
                subKegiatanOpd.lastModifiedDate()
        );
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
