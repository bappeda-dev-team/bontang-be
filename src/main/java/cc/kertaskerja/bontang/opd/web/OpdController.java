package cc.kertaskerja.bontang.opd.web;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanDto;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanService;
import cc.kertaskerja.bontang.bidangurusan.web.BidangUrusanRequest;
import cc.kertaskerja.bontang.opd.domain.Opd;
import cc.kertaskerja.bontang.opd.domain.OpdService;
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
@Tag(name = "opd")
@RequestMapping("opd")
public class OpdController {
    private final OpdService opdService;
    private final BidangUrusanService bidangUrusanService;

    public OpdController(OpdService opdService, BidangUrusanService bidangUrusanService) {
        this.opdService = opdService;
        this.bidangUrusanService = bidangUrusanService;
    }

    /**
     * Ambil data berdasarkan kode opd
     * @param kodeOpd
     */
    @GetMapping("detail/{kodeOpd}")
    public Opd getByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        return opdService.detailOpdByKodeOpd(kodeOpd);
    }

    /**
     * Ambil semua data opd
     */
    @GetMapping("detail/findall")
    public List<OpdResponse> findAll() {
        Iterable<Opd> opds = opdService.findAll();
        return StreamSupport.stream(opds.spliterator(), false)
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Ambil dropdown semua bidang urusan tanpa perlu kode opd
     */
    @GetMapping("detail/bidang-urusan-findall")
    public Iterable<String> bidangUrusanFindAll() {
        List<BidangUrusanDto> all = bidangUrusanService.findAll();
        return all.stream()
                .map(BidangUrusanDto::namaBidangUrusan)
                .toList();
    }

    /**
     * Ambil semua bidang urusan yang sudah disimpan untuk suatu opd
     * @param kodeOpd
     */
    @GetMapping("detail/{kodeOpd}/list-bidang-urusan-saved")
    public Iterable<String> bidangUrusanSaved(@PathVariable("kodeOpd") String kodeOpd) {
        opdService.detailOpdByKodeOpd(kodeOpd);
        Iterable<BidangUrusan> saved = bidangUrusanService.findByKodeOpd(kodeOpd);
        return StreamSupport.stream(saved.spliterator(), false)
                .map(BidangUrusan::namaBidangUrusan)
                .toList();
    }

    /**
     * Simpan bidang urusan terpilih untuk suatu opd
     * @param kodeOpd
     */
    @PostMapping("{kodeOpd}/bidang-urusan")
    public ResponseEntity<BidangUrusan> pilihBidangUrusan(
            @PathVariable("kodeOpd") String kodeOpd,
            @Valid @RequestBody BidangUrusanRequest request
    ) {
        opdService.detailOpdByKodeOpd(kodeOpd);
        var saved = bidangUrusanService.simpanBidangUrusan(kodeOpd, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Ubah data opd berdasarkan kode opd
     * @param kodeOpd
     */
    @PutMapping("update/{kodeOpd}")
    public OpdResponse put(@PathVariable("kodeOpd") String kodeOpd, @Valid @RequestBody OpdRequest request) {
        Opd existingOpd = opdService.detailOpdByKodeOpd(kodeOpd);

        Opd opd = new Opd(
                existingOpd.id(),
                request.kodeOpd(),
                request.namaOpd(),
                existingOpd.createdDate(),
                null
        );

        Opd updated = opdService.ubahOpd(kodeOpd, opd);
        boolean kodeOpdBerubah = !existingOpd.kodeOpd().equals(request.kodeOpd());

        if (request.bidangUrusan() != null) {
            bidangUrusanService.simpanAtauPerbaruiBidangUrusan(
                    kodeOpd,
                    request.kodeOpd(),
                    request.bidangUrusan()
            );
        } else if (kodeOpdBerubah) {
            bidangUrusanService.pindahBidangUrusanKeOpd(kodeOpd, request.kodeOpd());
        }

        return mapToResponse(updated);
    }

    /**
     * Tambah data opd
     * @param request
     */
    @PostMapping
    public ResponseEntity<Opd> post(@Valid @RequestBody OpdRequest request) {
        Opd opd = Opd.of(
                request.kodeOpd(),
                request.namaOpd()
        );
        Opd saved = opdService.tambahOpd(opd);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus opd berdasarkan kode opd
     * @param kodeOpd
     */
    @DeleteMapping("delete/{kodeOpd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeOpd") String kodeOpd) {
        opdService.hapusOpd(kodeOpd);
    }

    private OpdResponse mapToResponse(Opd opd) {
        List<OpdBidangUrusanResponse> bidangUrusan = StreamSupport.stream(
                        bidangUrusanService.findByKodeOpd(opd.kodeOpd()).spliterator(),
                        false
                )
                .map(this::mapBidangUrusan)
                .toList();

        return new OpdResponse(
                opd.id(),
                opd.kodeOpd(),
                opd.namaOpd(),
                opd.createdDate(),
                opd.lastModifiedDate(),
                bidangUrusan
        );
    }

    private OpdBidangUrusanResponse mapBidangUrusan(BidangUrusan bidangUrusan) {
        return new OpdBidangUrusanResponse(
                bidangUrusan.id(),
                bidangUrusan.kodeBidangUrusan(),
                bidangUrusan.namaBidangUrusan()
        );
    }
}