package cc.kertaskerja.bontang.programprioritasanggaran.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("program_prioritas_anggaran")
public record ProgramPrioritasAnggaran(
        @Id
        Long id,

        @Column("id_program_prioritas")
        Long idProgramPrioritas,

        @Column("kode_opd")
        String kodeOpd,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static ProgramPrioritasAnggaran of (
            Long idProgramPrioritas,
            String kodeOpd
    ) {
        return new ProgramPrioritasAnggaran(
                null,
                idProgramPrioritas,
                kodeOpd,
                null,
                null
        );
    }
}