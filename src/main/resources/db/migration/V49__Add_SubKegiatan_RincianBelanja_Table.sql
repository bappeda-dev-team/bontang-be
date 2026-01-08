CREATE TABLE IF NOT EXISTS subkegiatan_rincian_belanja (
	id					    BIGSERIAL PRIMARY KEY NOT NULL,
    id_rincian_belanja      BIGINT NOT NULL,
    kode_subkegiatan        VARCHAR(255) NOT NULL UNIQUE,
	nama_subkegiatan        VARCHAR(255) NOT NULL,
    pagu                    INTEGER,
    created_date            timestamp NOT NULL default now(),
    last_modified_date      timestamp NOT NULL default now(),
    CONSTRAINT fk_subkegiatan_rincian_belanja FOREIGN KEY (id_rincian_belanja) REFERENCES rincian_belanja(id) ON DELETE CASCADE
);