package cc.kertaskerja.bontang.opd.web;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;

import java.time.Instant;
import java.util.List;

public record OpdResponse(
        Long id,
        String kodeOpd,
        String namaOpd,
        Instant createdDate,
        Instant lastModifiedDate,
        List<OpdBidangUrusanResponse> bidangUrusan
) {
}
