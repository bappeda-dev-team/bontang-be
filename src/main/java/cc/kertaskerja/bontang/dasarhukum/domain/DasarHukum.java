package cc.kertaskerja.bontang.dasarhukum.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "dasar_hukum")
public record DasarHukum(
        @Id
        Long id,

        @Column("peraturan_terkait")
        String peraturanTerkait,

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
    public static DasarHukum of (
            String peraturanTerkait,
            String uraian,
            String kodeOpd,
            Integer tahun
    ) {
        return new DasarHukum(
                null,
                peraturanTerkait,
                uraian,
                kodeOpd,
                tahun,
                null,
                null
        );
    }
}
