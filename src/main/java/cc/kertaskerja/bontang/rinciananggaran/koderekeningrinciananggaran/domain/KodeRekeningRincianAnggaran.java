package cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "kode_rekening_rincian_anggaran")
public record KodeRekeningRincianAnggaran(
        @Id
        Long id,

        @Column("id_rincian_anggaran")
        Integer idRincianAnggaran,

        @Column("id_kode_rekening")
        Long idKodeRekening,

        @Column("kode_rekening")
        String kodeRekening,

        @Column("nama_rekening")
        String namaRekening,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static KodeRekeningRincianAnggaran of (
            Integer idRincianAnggaran,
            Long idKodeRekening,
            String kodeRekening,
            String namaRekening
    ) {
        return new KodeRekeningRincianAnggaran(
                null,
                idRincianAnggaran,
                idKodeRekening,
                kodeRekening,
                namaRekening,
                null,
                null
        );
    }
}
