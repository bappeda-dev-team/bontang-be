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

        @Column("opd_id")
        Long opdId,

        @Column("nama_pegawai")
        String namaPegawai,

        @Column("nip")
        String nip,

        @Column("email")
        String email,

        @Column("jabatan_dinas")
        String jabatanDinas,

        @Column("jabatan_tim")
        String jabatanTim,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Pegawai of (
            String namaPegawai,
            String nip,
            String email,
            String jabatanDinas,
            String jabatanTim,
            Long opdId
    ) {
        return new Pegawai(
                null,
                opdId,
                namaPegawai,
                nip,
                email,
                jabatanDinas,
                jabatanTim,
                null,
                null
        );
    } 
}
