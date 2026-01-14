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

        @Column("id_sub_kegiatan_opd")
        Integer idSubKegiatanOpd,

        @Column("program_prioritas")
        String programPrioritas,

        Integer tahun,

        String keterangan,

        @Column("periode_tahun_awal")
        Integer periodeTahunAwal,

        @Column("periode_tahun_akhir")
        Integer periodeTahunAkhir,

        String status,

        @Column("kode_opd")
        String kodeOpd,

        @Column("kode_sub_kegiatan_opd")
        String kodeSubKegiatanOpd,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static ProgramPrioritas of (
            Integer idSubKegiatanOpd,
            String programPrioritas,
            Integer tahun,
            String keterangan,
            Integer periodeTahunAwal,
            Integer periodeTahunAkhir,
            String status,
            String kodeOpd,
            String kodeSubKegiatanOpd
    ) {
        return new ProgramPrioritas(
                null,
                idSubKegiatanOpd,
                programPrioritas,
                tahun,
                keterangan,
                periodeTahunAwal,
                periodeTahunAkhir,
                status,
                kodeOpd,
                kodeSubKegiatanOpd,
                null,
                null
        );
    }
}
