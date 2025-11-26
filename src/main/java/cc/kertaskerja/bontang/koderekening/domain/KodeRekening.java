package cc.kertaskerja.bontang.koderekening.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "kode_rekening")
public record KodeRekening(
        @Id
        Long id,

        @Column("kode_rekening")
        String kodeRekening,

        @Column("nama_rekening")
        String namaRekening,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static KodeRekening of (
            String kodeKodeRekening,
            String namaKodeRekening
    ) {
        return new KodeRekening(
                null,
                kodeKodeRekening,
                namaKodeRekening,
                null,
                null
        );
    }
}
