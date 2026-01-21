package cc.kertaskerja.bontang.programprioritasanggaran.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("program_prioritas_anggaran_rencana_kinerja")
public record ProgramPrioritasAnggaranRencanaKinerja(
        @Id
        Long id,

        @Column("id_program_prioritas_anggaran")
        Long idProgramPrioritasAnggaran,

        @Column("id_rencana_kinerja")
        Long idRencanaKinerja,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static ProgramPrioritasAnggaranRencanaKinerja of(
            Long idProgramPrioritasAnggaran,
            Long idRencanaKinerja
    ) {
        return new ProgramPrioritasAnggaranRencanaKinerja(
                null,
                idProgramPrioritasAnggaran,
                idRencanaKinerja,
                null,
                null
        );
    }
}
