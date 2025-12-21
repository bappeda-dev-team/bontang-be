package cc.kertaskerja.bontang.rencanaaksi.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rencana_aksi")
public record RencanaAksi(
        @Id
        Long id,

        @Column("id_rekin")
        Integer idRencanaKinerja,

        @Column("kode_opd")
        String kodeOpd,

        @Column("urutan")
        Integer urutan,

        @Column("nama_rencana_aksi")
        String namaRencanaAksi,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static RencanaAksi of(
            Integer idRencanaKinerja,
            String kodeOpd,
            Integer urutan,
            String namaRencanaAksi
    ) {
        return new RencanaAksi(
                null,
                idRencanaKinerja,
                kodeOpd,
                urutan,
                namaRencanaAksi,
                null,
                null
        );
    }
}
