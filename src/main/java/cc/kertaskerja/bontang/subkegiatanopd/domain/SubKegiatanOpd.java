package cc.kertaskerja.bontang.subkegiatanopd.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="subkegiatan_opd")
public record SubKegiatanOpd(
        @Id
		Long id,

		@Column("kode_subkegiatan")
		String kodeSubKegiatanOpd,

		@Column("nama_subkegiatan")
		String namaSubKegiatanOpd,

		@Column("kode_opd")
		String kodeOpd,

		@Column("tahun")
		Integer tahun,

		@CreatedDate
		Instant createdDate,

		@LastModifiedDate
		Instant lastModifiedDate
) {
    public static SubKegiatanOpd of (
			String kodeSubKegiatanOpd,
			String namaSubKegiatanOpd,
			String kodeOpd,
			Integer tahun
	) {
		return new SubKegiatanOpd(
				null,
				kodeSubKegiatanOpd,
				namaSubKegiatanOpd,
				kodeOpd,
				tahun,
				null,
				null
		);
	}
}
