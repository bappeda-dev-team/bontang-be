package cc.kertaskerja.bontang.kegiatanopd.web;

import cc.kertaskerja.bontang.kegiatan.domain.Kegiatan;
import cc.kertaskerja.bontang.kegiatan.domain.KegiatanService;
import cc.kertaskerja.bontang.kegiatan.web.request.KegiatanBatchRequest;
import cc.kertaskerja.bontang.kegiatanopd.domain.KegiatanOpd;
import cc.kertaskerja.bontang.kegiatanopd.domain.KegiatanOpdService;
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
@Tag(name = "kegiatan opd")
@RequestMapping("kegiatanopd")
public class KegiatanOpdController {
    private final KegiatanOpdService kegiatanOpdService;
    private final KegiatanService kegiatanService;

    public KegiatanOpdController(KegiatanOpdService kegiatanOpdService, KegiatanService kegiatanService) {
        this.kegiatanOpdService = kegiatanOpdService;
        this.kegiatanService = kegiatanService;
    }

    // get kegiatan opd by kode kegiatan opd
    @GetMapping("detail/{kodeKegiatanOpd}")
    public KegiatanOpd getByKodeKegiatanOpd(@PathVariable("kodeKegiatanOpd") String kodeKegiatanOpd) {
        return kegiatanOpdService.detailKegiatanOpdByKodeKegiatan(kodeKegiatanOpd);
    }

    // get kegiatan opd by kode opd dan tahun
    @GetMapping("detail/kodeOpd/{kodeOpd}/tahun/{tahun}")
    public List<KegiatanOpdResponse> findByKodeOpdAndTahun(
            @PathVariable("kodeOpd") String kodeOpd,
            @PathVariable("tahun") Integer tahun) {
        Iterable<KegiatanOpd> kegiatanOpds = kegiatanOpdService.findByKodeOpdAndTahun(kodeOpd, tahun);
        return StreamSupport.stream(kegiatanOpds.spliterator(), false)
                .map(this::map)
                .toList();
    }

    @GetMapping("opd/{kodeOpd}")
    public List<KegiatanOpdResponse> findByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        List<Kegiatan> kegiatans = kegiatanService.findKegiatansForKodeOpd(kodeOpd);
        return kegiatans.stream()
                .map(this::mapFromKegiatan)
                .toList();
    }

    // update kegiatan opd by kode kegiatan opd
    @PutMapping("update/{kodeKegiatanOpd}")
    public KegiatanOpd put(@PathVariable("kodeKegiatanOpd") String kodeKegiatanOpd, @Valid @RequestBody KegiatanOpdRequest request) {
        KegiatanOpd existingKegiatanOpd = kegiatanOpdService.detailKegiatanOpdByKodeKegiatan(kodeKegiatanOpd);

        KegiatanOpd kegiatanOpd = new KegiatanOpd(
                existingKegiatanOpd.id(),
                request.kodeKegiatanOpd(),
                request.namaKegiatanOpd(),
                request.kodeOpd(),
                request.tahun(),
                existingKegiatanOpd.createdDate(),
                null
        );

        return kegiatanOpdService.ubahKegiatanOpd(kodeKegiatanOpd, kegiatanOpd);
    }

    // create kegiatan opd
    @PostMapping
    public ResponseEntity<KegiatanOpd> post(@Valid @RequestBody KegiatanOpdRequest request) {
        KegiatanOpd kegiatanOpd = KegiatanOpd.of(
                request.kodeKegiatanOpd(),
                request.namaKegiatanOpd(),
                request.kodeOpd(),
                request.tahun()
        );
        KegiatanOpd saved = kegiatanOpdService.tambahKegiatanOpd(kegiatanOpd);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    // ambil data kegiatan opd berdasarkan kumpulan kode kegiatan
    @PostMapping("/find/batch/kode-kegiatan")
    public List<Kegiatan> findKegiatanBatch(@Valid @RequestBody KegiatanBatchRequest request) {
        List<Kegiatan> kegiatans = kegiatanService.detailKegiatanByKodeKegiatanIn(request.kodeKegiatan());
        kegiatans.forEach(kegiatanOpdService::simpanKegiatanOpd);
        return kegiatans;
    }

    // hapus kegiatan opd
    @DeleteMapping("delete/{kodeKegiatanOpd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeKegiatanOpd") String kodeKegiatanOpd) {
        kegiatanOpdService.hapusKegiatanOpd(kodeKegiatanOpd);
    }

    private KegiatanOpdResponse map(KegiatanOpd kegiatanOpd) {
        return new KegiatanOpdResponse(
                kegiatanOpd.id(),
                kegiatanOpd.kodeKegiatanOpd(),
                kegiatanOpd.namaKegiatanOpd(),
                kegiatanOpd.kodeOpd(),
                kegiatanOpd.tahun(),
                kegiatanOpd.createdDate(),
                kegiatanOpd.lastModifiedDate()
        );
    }

    private KegiatanOpdResponse mapFromKegiatan(Kegiatan kegiatan) {
        return new KegiatanOpdResponse(
                kegiatan.id(),
                kegiatan.kodeKegiatan(),
                kegiatan.namaKegiatan(),
                kegiatan.kodeOpd(),
                kegiatan.tahun(),
                kegiatan.createdDate(),
                kegiatan.lastModifiedDate()
        );
    }
}
