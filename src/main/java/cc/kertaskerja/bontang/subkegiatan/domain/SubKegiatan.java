package cc.kertaskerja.bontang.subkegiatan.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "subkegiatan")
public record SubKegiatan(
        @Id
        Long id,

        @Column("kode_subkegiatan")
        String kodeSubKegiatan,

        @Column("nama_subkegiatan")
        String namaSubKegiatan,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static SubKegiatan of (
            String kodeSubKegiatan,
            String namaSubKegiatan
    ) {
        return new SubKegiatan(
                null,
                kodeSubKegiatan,
                namaSubKegiatan,
                null,
                null
        );
    }
}
