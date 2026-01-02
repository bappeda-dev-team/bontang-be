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

    @Column("id_sumber_dana")
    Long idSumberDana,

    @Column("rencana_kinerja")
    String rencanaKinerja,

    @Column("kode_opd")
    String kodeOpd,

    @Column("nip")
    String nipPegawai,

    @Column("created_by")
    String createdBy,

    @Column("tahun")
    Integer tahun,

    @Column("status_rencana_kinerja")
    String statusRencanaKinerja,

    @Column("nama_opd")
    String namaOpd,

    @Column("nama_pegawai")
    String namaPegawai,

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
            Long idSumberDana,
            String rencanaKinerja,
            String kodeOpd,
            String nipPegawai,
            String createdBy,
            Integer tahun,
            String statusRencanaKinerja,
            String namaOpd,
            String namaPegawai,
            String sumberDana,
            String keterangan
    ) {
        return new RencanaKinerja(
            null,
            idSumberDana,
            rencanaKinerja,
            kodeOpd,
            nipPegawai,
            createdBy,
            tahun,
            statusRencanaKinerja,
            namaOpd,
            namaPegawai,
            sumberDana,
            keterangan,
            null,
            null
        );
    }

    public RencanaKinerja withId(Long id) {
        return new RencanaKinerja(
            id,
            this.idSumberDana,
            this.rencanaKinerja,
            this.kodeOpd,
            this.nipPegawai,
            this.createdBy,
            this.tahun,
            this.statusRencanaKinerja,
            this.namaOpd,
            this.namaPegawai,
            this.sumberDana,
            this.keterangan,
            this.createdDate,
            this.lastModifiedDate
        );
    }
}
