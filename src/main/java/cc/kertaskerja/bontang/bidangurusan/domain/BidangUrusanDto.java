package cc.kertaskerja.bontang.bidangurusan.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BidangUrusanDto(
        Long id,
        String kodeBidangUrusan,
        String namaBidangUrusan
) {
}
