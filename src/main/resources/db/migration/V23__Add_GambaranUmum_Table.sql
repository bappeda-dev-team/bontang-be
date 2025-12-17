CREATE TABLE IF NOT EXISTS gambaran_umum (
	id					    BIGSERIAL PRIMARY KEY NOT NULL,
    gambaran_umum           VARCHAR(255) NOT NULL,
	uraian                  VARCHAR(255) NOT NULL,
    kode_opd                VARCHAR(255),
    tahun                   INTEGER,
    created_date            timestamp NOT NULL default now(),
    last_modified_date      timestamp NOT NULL default now()
);
