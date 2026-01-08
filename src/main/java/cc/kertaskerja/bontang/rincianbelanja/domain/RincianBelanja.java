package cc.kertaskerja.bontang.rincianbelanja.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rincian_belanja")
public record RincianBelanja(
        @Id
        Long id,

        @Column("id_sumber_dana")
        Long idSumberDana,

        @Column("id_rencana_kinerja")
        Long idRencanaKinerja,

        @Column("id_rencana_aksi")
        Long idRencanaAksi,

        @Column("kode_opd")
        String kodeOpd,

        @Column("nama_opd")
        String namaOpd,

        @Column("tahun")
        Integer tahun,

        @Column("status_rincian_belanja")
        String statusRincianBelanja,

        @Column("nip_pegawai")
        String nipPegawai,

        @Column("nama_pegawai")
        String namaPegawai,

        @Column("sumber_dana")
        String sumberDana,

        @Column("rencana_kinerja")
        String rencanaKinerja,

        @Column("rencana_aksi")
        String rencanaAksi,

        @Column("total_anggaran")
        Integer totalAnggaran,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static RincianBelanja of (
            Long id,
            Long idSumberDana,
            Long idRencanaKinerja,
            Long idRencanaAksi,
            String kodeOpd,
            String namaOpd,
            Integer tahun,
            String statusRincianBelanja,
            String nip,
            String namaPegawai,
            String sumberDana,
            String rencanaKinerja,
            String rencanaAksi,
            Integer totalAnggaran
    ) {
        return new RincianBelanja(
                null,
                idSumberDana,
                idRencanaKinerja,
                idRencanaAksi,
                kodeOpd,
                namaOpd,
                tahun,
                statusRincianBelanja,
                nip,
                namaPegawai,
                sumberDana,
                rencanaKinerja,
                rencanaAksi,
                totalAnggaran,
                null,
                null
        );
    }
}
