package cc.kertaskerja.bontang.gambaranumum.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "gambaran_umum")
public record GambaranUmum(
        @Id
        Long id,

        @Column("id_rencana_kinerja")
        Long idRencanaKinerja,

        @Column("gambaran_umum")
        String gambaranUmum,

        @Column("uraian")
        String uraian,

        @Column("kode_opd")
        String kodeOpd,

        @Column("tahun")
        Integer tahun,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static GambaranUmum of (
            Long idRencanaKinerja,
            String gambaranUmum,
            String uraian,
            String kodeOpd,
            Integer tahun
    ) {
        return new GambaranUmum(
                null,
                idRencanaKinerja,
                gambaranUmum,
                uraian,
                kodeOpd,
                tahun,
                null,
                null
        );
    }
}