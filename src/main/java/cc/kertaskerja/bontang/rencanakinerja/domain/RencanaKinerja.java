package cc.kertaskerja.bontang.rencanakinerja.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rencana_kinerja")
public record RencanaKinerja(
    @Id
    Long id,

    @Column("rencana_kinerja")
    String rencanaKinerja,

    @Column("indikator")
    String indikator,

    @Column("target")
    String target,

    @Column("sumber_dana")
    String sumberDana,

    @Column("keterangan")
    String keterangan,

    @CreatedDate
    Instant createdDate,

    @LastModifiedDate
    Instant lastModifiedDate
) {
    public static RencanaKinerja of (
            String rencanaKinerja,
            String indikator,
            String target,
            String sumberDana,
            String keterangan
    ) {
        return new RencanaKinerja(
            null,
            rencanaKinerja,
            indikator,
            target,
            sumberDana,
            keterangan,
            null,
            null
        );
    }
}
