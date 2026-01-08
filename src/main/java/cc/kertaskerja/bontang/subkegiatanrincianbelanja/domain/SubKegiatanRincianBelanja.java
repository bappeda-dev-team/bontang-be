package cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="subkegiatan_rincian_belanja")
public record SubKegiatanRincianBelanja(
        @Id
		Long id,

        @Column("id_rincian_belanja")
        Integer idRincianBelanja,

		@Column("kode_subkegiatan")
		String kodeSubKegiatanRincianBelanja,

		@Column("nama_subkegiatan")
		String namaSubKegiatanRincianBelanja,

        @Column("pagu")
        Integer pagu,

		@CreatedDate
		Instant createdDate,

		@LastModifiedDate
		Instant lastModifiedDate
) {
    public static SubKegiatanRincianBelanja of (
            Integer idRincianBelanja,
            String kodeSubKegiatanRincianBelanja,
            String namaSubKegiatanRincianBelanja,
            Integer pagu
    ) {
        return new SubKegiatanRincianBelanja (
            null,
            idRincianBelanja,
            kodeSubKegiatanRincianBelanja,
            namaSubKegiatanRincianBelanja,
            pagu,
            null,
            null
        );
    }
}
