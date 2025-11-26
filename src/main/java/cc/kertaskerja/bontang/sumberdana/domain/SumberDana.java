package cc.kertaskerja.bontang.sumberdana.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "sumber_dana")
public record SumberDana(
        @Id
        Long id,

        @Column("kode_dana_lama")
        String kodeDanaLama,

        @Column("sumber_dana")
        String sumberDana,

        @Column("kode_dana_baru")
        String kodeDanaBaru,

        @Column("set_input")
        SetInput setInput,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static SumberDana of (
            String kodeDanaLama,
            String sumberDana,
            String kodeDanaBaru,
            SetInput setInput
    ) {
        return new SumberDana(
                null,
                kodeDanaLama,
                sumberDana,
                kodeDanaBaru,
                setInput,
                null,
                null
        );
    }
}
