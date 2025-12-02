package cc.kertaskerja.bontang.bidangurusan.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanAlreadyExistException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanDeleteForbiddenException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import cc.kertaskerja.bontang.opd.web.OpdBidangUrusanRequest;
import cc.kertaskerja.bontang.program.domain.ProgramRepository;
import cc.kertaskerja.bontang.bidangurusan.web.BidangUrusanRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class BidangUrusanService {
    private final WebClient towerDataWebClient;
    private final BidangUrusanRepository bidangUrusanRepository;
    private final ProgramRepository programRepository;

    public BidangUrusanService(
            @Qualifier("towerDataWebClient") WebClient towerDataWebClient,
            BidangUrusanRepository bidangUrusanRepository,
            ProgramRepository programRepository
    ) {
        this.towerDataWebClient = towerDataWebClient;
        this.bidangUrusanRepository = bidangUrusanRepository;
        this.programRepository = programRepository;
    }

    public List<BidangUrusanDto> findAll() {
        return fetchAllFromTower();
    }

    public Iterable<BidangUrusan> findByKodeOpd(String kodeOpd) {
        return bidangUrusanRepository.findByKodeOpd(kodeOpd);
    }

    public BidangUrusan simpanBidangUrusan(String kodeOpd, BidangUrusanRequest request) {
        BidangUrusanDto towerData = cariBidangUrusanTowerByNama(request.namaBidangUrusan())
                .orElseThrow(() -> new BidangUrusanNotFoundException(request.namaBidangUrusan()));

        if (bidangUrusanRepository.existsByKodeOpdAndKodeBidangUrusan(kodeOpd, towerData.kodeBidangUrusan())) {
            throw new BidangUrusanAlreadyExistException(towerData.kodeBidangUrusan(), kodeOpd);
        }

        BidangUrusan bidangUrusan = BidangUrusan.of(
                kodeOpd,
                towerData.kodeBidangUrusan(),
                towerData.namaBidangUrusan()
        );

        return bidangUrusanRepository.save(bidangUrusan);
    }

    public List<BidangUrusan> simpanAtauPerbaruiBidangUrusan(
            String existingKodeOpd,
            String targetKodeOpd,
            List<OpdBidangUrusanRequest> requests
    ) {
        if (requests == null || requests.isEmpty()) {
            return List.of();
        }

        String kodeOpd = StringUtils.hasText(targetKodeOpd) ? targetKodeOpd : existingKodeOpd;

        return requests.stream()
                .map(request -> simpanAtauPerbaruiBidangUrusan(kodeOpd, request))
                .toList();
    }

    private BidangUrusan simpanAtauPerbaruiBidangUrusan(String kodeOpd, OpdBidangUrusanRequest request) {
        if (request.id() != null) {
            BidangUrusan existing = bidangUrusanRepository.findById(request.id())
                    .orElseThrow(() -> new BidangUrusanNotFoundException("ID " + request.id()));
            validateDuplicate(kodeOpd, request.kodeBidangUrusan(), request.id());

            BidangUrusan updated = new BidangUrusan(
                    existing.id(),
                    kodeOpd,
                    request.kodeBidangUrusan(),
                    request.namaBidangUrusan(),
                    existing.createdDate(),
                    null
            );
            return bidangUrusanRepository.save(updated);
        }

        if (bidangUrusanRepository.existsByKodeOpdAndKodeBidangUrusan(kodeOpd, request.kodeBidangUrusan())) {
            throw new BidangUrusanAlreadyExistException(request.kodeBidangUrusan(), kodeOpd);
        }

        BidangUrusan bidangUrusan = new BidangUrusan(
                null,
                kodeOpd,
                request.kodeBidangUrusan(),
                request.namaBidangUrusan(),
                null,
                null
        );

        return bidangUrusanRepository.save(bidangUrusan);
    }

    private void validateDuplicate(String kodeOpd, String kodeBidangUrusan, Long currentId) {
        Iterable<BidangUrusan> existing = bidangUrusanRepository.findByKodeOpd(kodeOpd);
        for (BidangUrusan data : existing) {
            if (!data.id().equals(currentId) && data.kodeBidangUrusan().equalsIgnoreCase(kodeBidangUrusan)) {
                throw new BidangUrusanAlreadyExistException(kodeBidangUrusan, kodeOpd);
            }
        }
    }

    public void updateNamaBidangUrusan(String kodeOpd, String namaBidangUrusan) {
        Iterable<BidangUrusan> bidangUrusans = bidangUrusanRepository.findByKodeOpd(kodeOpd);
        boolean hasData = false;

        for (BidangUrusan bidangUrusan : bidangUrusans) {
            hasData = true;
            BidangUrusan updated = new BidangUrusan(
                    bidangUrusan.id(),
                    bidangUrusan.kodeOpd(),
                    bidangUrusan.kodeBidangUrusan(),
                    namaBidangUrusan,
                    bidangUrusan.createdDate(),
                    null
            );
            bidangUrusanRepository.save(updated);
        }

        if (!hasData) {
            throw new BidangUrusanNotFoundException("Kode OPD " + kodeOpd);
        }
    }

    private List<BidangUrusanDto> fetchAllFromTower() {
        List<BidangUrusanDto> response = towerDataWebClient.get()
                .uri("/bidangurusan/detail/findall")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BidangUrusanDto>>() {})
                .block();

        return Optional.ofNullable(response)
                .orElseGet(List::of);
    }

    private Optional<BidangUrusanDto> cariBidangUrusanTowerByNama(String namaBidangUrusan) {
        return fetchAllFromTower()
                .stream()
                .filter(item -> namaBidangUrusan.equalsIgnoreCase(item.namaBidangUrusan()))
                .findFirst();
    }

    public void hapusBidangUrusan(String kodeBidangUrusan) {
        BidangUrusan bidangUrusan = bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)
                .orElseThrow(() -> new BidangUrusanNotFoundException(kodeBidangUrusan));

        if (programRepository.existsByBidangUrusanId(bidangUrusan.id())) {
            throw new BidangUrusanDeleteForbiddenException(kodeBidangUrusan);
        }

        bidangUrusanRepository.deleteById(bidangUrusan.id());
    }
}
