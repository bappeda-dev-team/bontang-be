package cc.kertaskerja.bontang.indikatorbelanja.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "indikator_belanja")
public record IndikatorBelanja(
        @Id
        Long id,

        @Column("nama_indikator_belanja")
        String namaIndikatorBelanja,

        @Column("id_rincian_belanja")
        Long rincianBelanjaId,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static IndikatorBelanja of (
            String namaIndikator,
            Long rencanaKinerjaId
    ) {
        return new IndikatorBelanja(
                null,
                namaIndikator,
                rencanaKinerjaId,
                null,
                null
        );
    }
}
