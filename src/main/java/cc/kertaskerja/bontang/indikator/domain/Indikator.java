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

        @Column("rencana_kinerja_id")
        Long rencanaKinerjaId,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Indikator of (
            String namaIndikator,
            Long rencanaKinerjaId
    ) {
        return new Indikator(
                null,
                namaIndikator,
                rencanaKinerjaId,
                null,
                null
        );
    }
}
