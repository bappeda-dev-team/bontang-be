package cc.kertaskerja.bontang.bidangurusan.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanAlreadyExistException;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import cc.kertaskerja.bontang.opd.web.OpdBidangUrusanRequest;
import cc.kertaskerja.bontang.bidangurusan.web.BidangUrusanRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    public List<BidangUrusan> simpanAtauPerbaruiBidangUrusan(
            String existingKodeOpd,
            String targetKodeOpd,
            List<OpdBidangUrusanRequest> requests
    ) {
        if (requests == null) {
            return List.of();
        }

        String kodeOpd = StringUtils.hasText(targetKodeOpd) ? targetKodeOpd : existingKodeOpd;

        Map<String, BidangUrusan> existingByKode = new LinkedHashMap<>();
        bidangUrusanRepository.findByKodeOpd(existingKodeOpd)
                .forEach(item -> {
                    if (item.id() != null) {
                        existingByKode.put(normalizeKode(item.kodeBidangUrusan()), item);
                    }
                });

        if (StringUtils.hasText(targetKodeOpd) && !targetKodeOpd.equals(existingKodeOpd)) {
            bidangUrusanRepository.findByKodeOpd(targetKodeOpd)
                    .forEach(item -> {
                        if (item.id() != null) {
                            existingByKode.putIfAbsent(normalizeKode(item.kodeBidangUrusan()), item);
                        }
                    });
        }

        List<BidangUrusan> saved = new ArrayList<>();
        Set<Long> retainedIds = new HashSet<>();

        for (OpdBidangUrusanRequest request : requests) {
            BidangUrusan matched = existingByKode.get(normalizeKode(request.kodeBidangUrusan()));

            BidangUrusan savedData = (matched != null)
                    ? perbaruiBidangUrusan(kodeOpd, request, matched)
                    : simpanBidangUrusanBaru(kodeOpd, request);
            saved.add(savedData);
            if (savedData.id() != null) {
                retainedIds.add(savedData.id());
            }
        }

        existingByKode.values().stream()
                .filter(item -> item.id() != null && !retainedIds.contains(item.id()))
                .forEach(this::hapusBidangUrusan);

        return saved;
    }

    private BidangUrusan perbaruiBidangUrusan(String kodeOpd, OpdBidangUrusanRequest request, BidangUrusan existing) {
        validateDuplicate(kodeOpd, request.kodeBidangUrusan(), existing.id());

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

    private BidangUrusan simpanBidangUrusanBaru(String kodeOpd, OpdBidangUrusanRequest request) {
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
            if (data.id() == null) {
                continue;
            }
            if (!data.id().equals(currentId) && data.kodeBidangUrusan().equalsIgnoreCase(kodeBidangUrusan)) {
                throw new BidangUrusanAlreadyExistException(kodeBidangUrusan, kodeOpd);
            }
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

    private String normalizeKode(String kodeBidangUrusan) {
        return kodeBidangUrusan == null ? "" : kodeBidangUrusan.trim().toLowerCase(Locale.ROOT);
    }

    public void pindahBidangUrusanKeOpd(String sumberKodeOpd, String targetKodeOpd) {
        if (!StringUtils.hasText(targetKodeOpd) || sumberKodeOpd.equals(targetKodeOpd)) {
            return;
        }

        Map<String, BidangUrusan> targetByKode = new HashMap<>();
        bidangUrusanRepository.findByKodeOpd(targetKodeOpd)
                .forEach(item -> {
                    if (item.id() != null) {
                        targetByKode.put(normalizeKode(item.kodeBidangUrusan()), item);
                    }
                });

        bidangUrusanRepository.findByKodeOpd(sumberKodeOpd)
                .forEach(item -> {
                    String key = normalizeKode(item.kodeBidangUrusan());
                    BidangUrusan duplicate = targetByKode.get(key);
                    if (duplicate != null && !duplicate.id().equals(item.id())) {
                        throw new BidangUrusanAlreadyExistException(item.kodeBidangUrusan(), targetKodeOpd);
                    }

                    BidangUrusan updated = new BidangUrusan(
                            item.id(),
                            targetKodeOpd,
                            item.kodeBidangUrusan(),
                            item.namaBidangUrusan(),
                            item.createdDate(),
                            null
                    );
                    bidangUrusanRepository.save(updated);
                });
    }
}
