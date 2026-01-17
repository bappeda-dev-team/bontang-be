package cc.kertaskerja.bontang.programprioritasopd.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("program_prioritas_opd")
public record ProgramPrioritasOpd(
        @Id
        Long id,

        @Column("id_program_prioritas")
        Long idProgramPrioritas,

        @Column("id_sub_kegiatan_opd")
        Long idSubkegiatanOpd,

        @Column("id_rencana_kinerja")
        Long idRencanaKinerja,

        Integer tahun,

        @Column("kode_opd")
        String kodeOpd,

        String status,

        String keterangan,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static ProgramPrioritasOpd of (
            Long idProgramPrioritas,
            Long idSubkegiatanOpd,
            Long idRencanaKinerja,
            Integer tahun,
            String kodeOpd,
            String status,
            String keterangan
    ) {
        return new ProgramPrioritasOpd(
                null,
                idProgramPrioritas,
                idSubkegiatanOpd,
                idRencanaKinerja,
                tahun,
                kodeOpd,
                status,
                keterangan,
                null,
                null
        );
    }
}
