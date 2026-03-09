package cc.kertaskerja.bontang.laporanverifikasi.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "laporan_verifikasi_catatan")
public record LaporanVerifikasiCatatan(
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

        @Column("tahap_verifikasi")
        String tahapVerifikasi,

        @Column("catatan")
        String catatan,

        @Column("catatan_by_nip")
        String catatanByNip,

        @CreatedDate
        @Column("created_date")
        Instant createdDate,

        @LastModifiedDate
        @Column("last_modified_date")
        Instant lastModifiedDate
) {
    public static LaporanVerifikasiCatatan of(
            String jenisLaporan,
            String kodeOpd,
            Integer tahun,
            String filterHash,
            String tahapVerifikasi,
            String catatan,
            String catatanByNip
    ) {
        return new LaporanVerifikasiCatatan(
                null,
                jenisLaporan,
                kodeOpd,
                tahun,
                filterHash,
                tahapVerifikasi,
                catatan,
                catatanByNip,
                null,
                null
        );
    }
}
