package cc.kertaskerja.bontang.pegawai.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("pegawai")
public record Pegawai(
        @Id
        Long id,

        @Column("kode_opd")
        String kodeOpd,

        @Column("tahun")
        Integer tahun,

        @Column("nama_pegawai")
        String namaPegawai,

        @Column("nip")
        String nip,

        @Column("email")
        String email,

        @Column("role")
        String role,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Pegawai of (
            String kodeOpd,
            Integer tahun,
            String namaPegawai,
            String nip,
            String email,
            String role
    ) {
        return new Pegawai(
                null,
                kodeOpd,
                tahun,
                namaPegawai,
                nip,
                email,
                role,
                null,
                null
        );
    }
}
