package cc.kertaskerja.bontang.programopd.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "program_opd")
public record ProgramOpd(
		@Id
		Long id,

		@Column("kode_program")
		String kodeProgramOpd,

		@Column("nama_program")
		String namaProgramOpd,

		@Column("kode_opd")
		String kodeOpd,

		@Column("tahun")
		Integer tahun,

		@CreatedDate
		Instant createdDate,

		@LastModifiedDate
		Instant lastModifiedDate 
) {
	public static ProgramOpd of (
			String kodeProgramOpd,
			String namaProgramOpd,
			String kodeOpd,
			Integer tahun
	) {
		return new ProgramOpd(
				null,
				kodeProgramOpd,
				namaProgramOpd,
				kodeOpd,
				tahun,
				null,
				null
		);
	}
}