CREATE TABLE IF NOT EXISTS pelaksanaan_rincian_anggaran (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
	id_rincian_anggaran	BIGINT NOT NULL,
	bulan				INTEGER NOT NULL,
	bobot				INTEGER,
	created_date		timestamp NOT NULL default now(),
	last_modified_date	timestamp NOT NULL default now(),
	CONSTRAINT fk_pelaksanaan_rincian_anggaran_rincian_anggaran FOREIGN KEY (id_rincian_anggaran) REFERENCES rincian_anggaran(id) ON DELETE CASCADE
);