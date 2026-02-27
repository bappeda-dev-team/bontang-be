package cc.kertaskerja.bontang.laporanverifikasi.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "laporan_verifikasi")
public record LaporanVerifikasi(
        @Id
        Long id,

        @Column("jenis_laporan")
        String jenisLaporan,

        @Column("kode_opd")
        String kodeOpd,

        @Column("tahun")
        Integer tahun,

        @Column("filter_hash")
        String filterHash,

        @Column("verified_by_nip")
        String verifiedByNip,

        @Column("verified_at")
        Instant verifiedAt,

        @CreatedDate
        @Column("created_date")
        Instant createdDate,

        @LastModifiedDate
        @Column("last_modified_date")
        Instant lastModifiedDate
) {
    public static LaporanVerifikasi of(
            String jenisLaporan,
            String kodeOpd,
            Integer tahun,
            String filterHash,
            String verifiedByNip,
            Instant verifiedAt
    ) {
        return new LaporanVerifikasi(
                null,
                jenisLaporan,
                kodeOpd,
                tahun,
                filterHash,
                verifiedByNip,
                verifiedAt,
                null,
                null
        );
    }
}
