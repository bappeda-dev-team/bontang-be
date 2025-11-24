package cc.kertaskerja.bontang.kegiatan.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

public record Kegiatan(
        @Id
        Long id,

        @Column("kode_kegiatan")
        String kodeKegiatan,

        @Column("nama_kegiatan")
        String namaKegiatan,

        @Column("kode_program")
        String kodeProgram,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Kegiatan of (
            String kodeKegiatan,
            String namaKegiatan,
            String kodeProgram
    ) {
        return new Kegiatan(
                null,
                kodeKegiatan,
                namaKegiatan,
                kodeProgram,
                null,
                null
        );
    }
}
