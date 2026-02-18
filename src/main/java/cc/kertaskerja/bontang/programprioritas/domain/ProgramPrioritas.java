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

        Integer tahun,

        String keterangan,

        @Column("periode_tahun_awal")
        Integer periodeTahunAwal,

        @Column("periode_tahun_akhir")
        Integer periodeTahunAkhir,

        String status,
        @Column("rencana_implementasi")
        String rencanaImplementasi,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static ProgramPrioritas of (
            String programPrioritas,
            Integer tahun,
            String keterangan,
            Integer periodeTahunAwal,
            Integer periodeTahunAkhir,
            String status,
            String rencanaImplementasi
    ) {
        return new ProgramPrioritas(
                null,
                programPrioritas,
                tahun,
                keterangan,
                periodeTahunAwal,
                periodeTahunAkhir,
                status,
                rencanaImplementasi,
                null,
                null
        );
    }
}
