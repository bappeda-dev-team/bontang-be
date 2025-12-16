package cc.kertaskerja.bontang.kegiatanopd.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "kegiatan_opd")
public record KegiatanOpd(
		@Id
		Long id,

		@Column("kode_kegiatan")
		String kodeKegiatanOpd,

		@Column("nama_kegiatan")
		String namaKegiatanOpd,

		@Column("kode_opd")
		String kodeOpd,

		@Column("tahun")
		Integer tahun,

		@CreatedDate
		Instant createdDate,

		@LastModifiedDate
		Instant lastModifiedDate 
) {
	public static KegiatanOpd of (
			String kodeKegiatanOpd,
			String namaKegiatanOpd,
			String kodeOpd,
			Integer tahun
	) {
		return new KegiatanOpd(
				null,
				kodeKegiatanOpd,
				namaKegiatanOpd,
				kodeOpd,
				tahun,
				null,
				null
		);
	}
}