package cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="subkegiatan_rencana_kinerja")
public record SubKegiatanRencanaKinerja(
        @Id
        Long id,

        @Column("id_rekin")
        Integer idRekin,

        @Column("kode_subkegiatan")
        String kodeSubKegiatan,

        @Column("nama_subkegiatan")
        String namaSubKegiatan
) {
    public static SubKegiatanRencanaKinerja of (
            Integer idRekin,
            String kodeSubKegiatan,
            String namaSubKegiatan 
    ) {
        return new SubKegiatanRencanaKinerja(
                null,
                idRekin,
                kodeSubKegiatan,
                namaSubKegiatan
        );
    }
}
