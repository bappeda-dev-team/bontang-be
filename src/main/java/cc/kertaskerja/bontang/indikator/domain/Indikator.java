package cc.kertaskerja.bontang.indikator.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "indikator")
public record Indikator(
        @Id
        Long id,

        @Column("nama_indikator")
        String namaIndikator,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Indikator of (
            String namaIndikator
    ) {
        return new Indikator(
                null,
                namaIndikator,
                null,
                null
        );
    }
}