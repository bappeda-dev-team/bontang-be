package cc.kertaskerja.bontang.bidangurusan.web;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanDto;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "bidangurusan")
@RequestMapping("bidangurusan")
public class BidangUrusanController {
    private final BidangUrusanService bidangUrusanService;

    public BidangUrusanController(BidangUrusanService bidangUrusanService) {
        this.bidangUrusanService = bidangUrusanService;
    }

    /**
     * Ambil semua bidang urusan dari Tower Data
     */
    @GetMapping("detail/findall")
    public List<BidangUrusanDto> findAll() {
        return bidangUrusanService.findAll();
    }

    /**
     * Ambil semua bidang urusan yang sudah disimpan untuk suatu OPD
     * @param kodeOpd
     */
    @GetMapping("detail/{kodeOpd}")
    public Iterable<BidangUrusan> findByKodeOpd(@PathVariable("kodeOpd") String kodeOpd) {
        return bidangUrusanService.findByKodeOpd(kodeOpd);
    }

    /**
     * Simpan bidang urusan terpilih untuk suatu OPD
     * @param kodeOpd
     */
    @PostMapping("{kodeOpd}")
    public ResponseEntity<BidangUrusan> post(
            @PathVariable("kodeOpd") String kodeOpd,
            @Valid @RequestBody BidangUrusanRequest request
    ) {
        BidangUrusan saved = bidangUrusanService.simpanBidangUrusan(kodeOpd, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Hapus bidang urusan berdasarkan kode bidang urusan
     * @param kodeBidangUrusan
     */
    @DeleteMapping("delete/{kodeBidangUrusan}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("kodeBidangUrusan") String kodeBidangUrusan) {
        bidangUrusanService.hapusBidangUrusan(kodeBidangUrusan);
    }
}
