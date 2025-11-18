CREATE TABLE IF NOT EXISTS opd (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
	kode_opd            VARCHAR(255) NOT NULL UNIQUE,
    nama_opd            VARCHAR(255) NOT NULL,
    tahun_opd           INTEGER,
    jenis_tahun_opd     VARCHAR(255),
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);