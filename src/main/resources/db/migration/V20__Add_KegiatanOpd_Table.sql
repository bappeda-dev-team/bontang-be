CREATE TABLE IF NOT EXISTS kegiatan_opd (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
    kode_kegiatan       VARCHAR(255) NOT NULL UNIQUE,
	nama_kegiatan       VARCHAR(255) NOT NULL,
    kode_opd            VARCHAR(255),
    tahun               INTEGER,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);
