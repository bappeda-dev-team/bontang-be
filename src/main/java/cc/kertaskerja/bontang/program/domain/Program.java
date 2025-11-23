package cc.kertaskerja.bontang.program.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

public record Program(
		@Id
        Long id,

        @Column("kode_program")
        String kodeProgram,

        @Column("nama_program")
        String namaProgram,

        @Column("kode_opd")
        String kodeOpd,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Program of (
            String kodeProgram,
            String namaProgram,
            String kodeOpd
    ) {
        return new Program(
                null,
                kodeProgram,
                namaProgram,
                kodeOpd,
                null,
                null
        );
    }
}
