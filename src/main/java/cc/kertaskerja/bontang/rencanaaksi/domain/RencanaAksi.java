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

    @Column("rencana_aksi")
    String rencanaAksi,

    @Column("urutan")
    Integer urutan,

    @CreatedDate
    Instant createdDate,

    @LastModifiedDate
    Instant lastModifiedDate
) {
    public static RencanaAksi of(
            String rencanaAksi,
            Integer urutan
    ) {
        return new RencanaAksi(
            null,
            rencanaAksi,
            urutan,
            null,
            null
        );
    }
}
