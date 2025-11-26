package cc.kertaskerja.bontang.programprioritas.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("program_prioritas")
public record ProgramPrioritas(
        @Id
        Long id,

        @Column("program_prioritas")
        String programPrioritas,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static ProgramPrioritas of (
            String programPrioritas
    ) {
        return new ProgramPrioritas(
                null,
                programPrioritas,
                null,
                null
        );
    }
}
