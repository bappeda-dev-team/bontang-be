package cc.kertaskerja.bontang.bidangurusan.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanAlreadyExistException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
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
        BidangUrusanDto towerData = cariBidangUrusanTower(request.namaBidangUrusan())
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

    private Optional<BidangUrusanDto> cariBidangUrusanTower(String identifier) {
        if (!StringUtils.hasText(identifier)) {
            return Optional.empty();
        }

        String keyword = identifier.trim();
        String numericKeyword = keyword.replaceAll("\\D", "");

        return fetchAllFromTower()
                .stream()
                .filter(item ->
                        keyword.equalsIgnoreCase(item.namaBidangUrusan()) ||
                                keyword.equalsIgnoreCase(item.kodeBidangUrusan()) ||
                                (item.id() != null && (
                                        keyword.equalsIgnoreCase(String.valueOf(item.id())) ||
                                                (!numericKeyword.isEmpty() && numericKeyword.equals(String.valueOf(item.id())))
                                ))
                )
                .findFirst();
    }

    public void hapusBidangUrusan(String kodeBidangUrusan) {
        BidangUrusan bidangUrusan = bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)
                .orElseThrow(() -> new BidangUrusanNotFoundException(kodeBidangUrusan));

        hapusBidangUrusan(bidangUrusan);
    }

    public void hapusBidangUrusan(String kodeOpd, String identifier) {
        BidangUrusan bidangUrusan = bidangUrusanRepository.findByKodeOpdAndKodeBidangUrusan(kodeOpd, identifier)
                .orElseGet(() -> cariBidangUrusanBerdasarkanTower(kodeOpd, identifier));

        hapusBidangUrusan(bidangUrusan);
    }

    private BidangUrusan cariBidangUrusanBerdasarkanTower(String kodeOpd, String identifier) {
        BidangUrusanDto towerData = cariBidangUrusanTower(identifier)
                .orElseThrow(() -> new BidangUrusanNotFoundException(identifier));

        return bidangUrusanRepository.findByKodeOpdAndKodeBidangUrusan(kodeOpd, towerData.kodeBidangUrusan())
                .orElseThrow(() -> new BidangUrusanNotFoundException(identifier));
    }

    private void hapusBidangUrusan(BidangUrusan bidangUrusan) {
        bidangUrusanRepository.deleteById(bidangUrusan.id());
    }
}
