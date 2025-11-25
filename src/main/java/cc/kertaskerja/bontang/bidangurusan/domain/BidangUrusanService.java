package cc.kertaskerja.bontang.bidangurusan.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanAlreadyExistException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import cc.kertaskerja.bontang.bidangurusan.web.BidangUrusanRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class BidangUrusanService {
    private final WebClient towerDataWebClient;
    private final BidangUrusanRepository bidangUrusanRepository;

    public BidangUrusanService(
            @Qualifier("towerDataWebClient") WebClient towerDataWebClient,
            BidangUrusanRepository bidangUrusanRepository
    ) {
        this.towerDataWebClient = towerDataWebClient;
        this.bidangUrusanRepository = bidangUrusanRepository;
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
}
