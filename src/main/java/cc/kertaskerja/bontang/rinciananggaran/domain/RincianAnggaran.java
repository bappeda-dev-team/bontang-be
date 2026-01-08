package cc.kertaskerja.bontang.rinciananggaran.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

public record RincianAnggaran(
        @Id
        Long id,

        @Column("id_rincian_belanja")
        Integer idRincianBelanja,
        
        @Column("rincian_anggaran")
        String namaRincianAnggaran,

        @Column("urutan")
        Integer urutan,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static RincianAnggaran of(
            Integer idRincianBelanja,
            String namaRincianAnggaran,
            Integer urutan
    ) {
        return new RincianAnggaran(
            null,
            idRincianBelanja,
            namaRincianAnggaran,
            urutan,
            null,
            null
        );
    }
}