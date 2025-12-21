package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "pelaksanaan_rencana_aksi")
public record Pelaksanaan(
        @Id
        Long id,

        @Column("id_rencana_aksi")
        Integer idRencanaAksi,

        @Column("bulan")
        Integer bulan,

        @Column("bobot")
        Integer bobot,

        @Column("bobot_tersedia")
        Integer bobotTersedia,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Pelaksanaan of(
            Integer idRencanaAksi,
            Integer bulan,
            Integer bobot
    ) {
        return new Pelaksanaan(
                null,
                idRencanaAksi,
                bulan,
                bobot,
                null,
                null,
                null
        );
    }

    public Pelaksanaan withBobotTersedia(Integer bobotTersedia) {
        return new Pelaksanaan(
                id(),
                idRencanaAksi(),
                bulan(),
                bobot(),
                bobotTersedia,
                createdDate(),
                lastModifiedDate()
        );
    }
}
