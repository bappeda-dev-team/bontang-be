package cc.kertaskerja.bontang.targetbelanja.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

public record TargetBelanja(
        @Id
        Long id,

        @Column("target_belanja")
        String namaTargetBelanja,

        @Column("satuan")
        String satuan,

        @Column("id_indikator_belanja")
        Long indikatorBelanjaId,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static TargetBelanja of (
            String namaTargetBelanja,
            String satuan,
            Long indikatorId
    ) {
        return new TargetBelanja(
                null,
                namaTargetBelanja,
                satuan,
                indikatorId,
                null,
                null
        );
    }
}
