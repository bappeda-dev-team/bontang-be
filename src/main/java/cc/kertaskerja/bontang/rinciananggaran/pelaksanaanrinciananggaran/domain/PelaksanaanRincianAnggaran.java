package cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "pelaksanaan_rincian_anggaran")
public record PelaksanaanRincianAnggaran(
        @Id
        Long id,

        @Column("id_rincian_anggaran")
        Integer idRincianAnggaran,

        @Column("bulan")
        Integer bulan,

        @Column("bobot")
        Integer bobot,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static PelaksanaanRincianAnggaran of(
            Integer idRincianAnggaran,
            Integer bulan,
            Integer bobot
    ) {
        return new PelaksanaanRincianAnggaran(
                null,
                idRincianAnggaran,
                bulan,
                bobot,
                null,
                null
        );
    }
}