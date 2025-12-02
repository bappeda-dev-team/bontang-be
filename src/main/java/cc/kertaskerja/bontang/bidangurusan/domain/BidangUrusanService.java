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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

        Map<Long, BidangUrusan> existing = new LinkedHashMap<>();
        bidangUrusanRepository.findByKodeOpd(existingKodeOpd)
                .forEach(item -> {
                    if (item.id() != null) {
                        existing.put(item.id(), item);
                    }
                });

        if (StringUtils.hasText(targetKodeOpd) && !targetKodeOpd.equals(existingKodeOpd)) {
            bidangUrusanRepository.findByKodeOpd(targetKodeOpd)
                    .forEach(item -> {
                        if (item.id() != null) {
                            existing.putIfAbsent(item.id(), item);
                        }
                    });
        }

        List<BidangUrusan> saved = new ArrayList<>();
        Set<Long> retainedIds = new HashSet<>();

        for (OpdBidangUrusanRequest request : requests) {
            BidangUrusan savedData = simpanAtauPerbaruiBidangUrusan(kodeOpd, request);
            saved.add(savedData);
            if (savedData.id() != null) {
                retainedIds.add(savedData.id());
            }
        }

        existing.values().stream()
                .filter(item -> item.id() != null && !retainedIds.contains(item.id()))
                .forEach(this::hapusBidangUrusan);

        return saved;
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

    private void hapusBidangUrusan(BidangUrusan bidangUrusan) {
        if (programRepository.existsByBidangUrusanId(bidangUrusan.id())) {
            throw new BidangUrusanDeleteForbiddenException(bidangUrusan.kodeBidangUrusan());
        }

        bidangUrusanRepository.deleteById(bidangUrusan.id());
    }
}
