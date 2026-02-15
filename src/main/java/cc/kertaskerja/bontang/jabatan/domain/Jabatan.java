package cc.kertaskerja.bontang.jabatan.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "jabatan")
public record Jabatan(
        @Id
        Long id,

        @Column("nama_jabatan")
        String namaJabatan,

        @Column("kode_jabatan")
        String kodeJabatan,

        @Column("jenis_jabatan")
        String jenisJabatan,

        @Column("kode_opd")
        String kodeOpd,

        @CreatedDate
        @Column("created_date")
        Instant createdDate,

        @LastModifiedDate
        @Column("last_modified_date")
        Instant lastModifiedDate
) {
    public static Jabatan of(
            String namaJabatan,
            String kodeJabatan,
            String jenisJabatan,
            String kodeOpd
    ) {
        return new Jabatan(
                null,
                namaJabatan,
                kodeJabatan,
                jenisJabatan,
                kodeOpd,
                null,
                null
        );
    }
}
