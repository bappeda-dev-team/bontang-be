package cc.kertaskerja.bontang.kegiatan.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "kegiatan")
public record Kegiatan(
        @Id
        Long id,

        @Column("kode_kegiatan")
        String kodeKegiatan,

        @Column("nama_kegiatan")
        String namaKegiatan,

        @Column("program_id")
        Long programId,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Kegiatan of (
            String kodeKegiatan,
            String namaKegiatan,
            Long programId
    ) {
        return new Kegiatan(
                null,
                kodeKegiatan,
                namaKegiatan,
                programId,
                null,
                null
        );
    }
}
