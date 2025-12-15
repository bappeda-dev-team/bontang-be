package cc.kertaskerja.bontang.program.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "program")
public record Program(
		@Id
        Long id,

        @Column("kode_program")
        String kodeProgram,

        @Column("nama_program")
        String namaProgram,

        @Column("kode_opd")
        String kodeOpd,

        @Column("tahun")
        Integer tahun,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Program of (
            String kodeProgram,
            String namaProgram,
            String kodeOpd,
            Integer tahun
    ) {
        return new Program(
                null,
                kodeProgram,
                namaProgram,
                kodeOpd,
                tahun,
                null,
                null
        );
    }
}
