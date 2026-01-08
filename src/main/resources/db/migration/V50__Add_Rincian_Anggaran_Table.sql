CREATE TABLE IF NOT EXISTS rincian_anggaran (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
    id_rincian_belanja  BIGINT NOT NULL,
    rincian_anggaran    VARCHAR(255) NOT NULL,
    urutan              INTEGER NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now(),
    CONSTRAINT fk_rincian_anggaran_rincian_belanja FOREIGN KEY (id_rincian_belanja) REFERENCES rincian_belanja(id) ON DELETE CASCADE
);