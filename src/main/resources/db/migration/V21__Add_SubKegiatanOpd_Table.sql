CREATE TABLE IF NOT EXISTS subkegiatan_opd (
	id					    BIGSERIAL PRIMARY KEY NOT NULL,
    kode_subkegiatan        VARCHAR(255) NOT NULL UNIQUE,
	nama_subkegiatan        VARCHAR(255) NOT NULL,
    kode_opd                VARCHAR(255),
    tahun                   INTEGER,
    created_date            timestamp NOT NULL default now(),
    last_modified_date      timestamp NOT NULL default now()
);
