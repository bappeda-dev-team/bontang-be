CREATE TABLE IF NOT EXISTS dasar_hukum (
	id					    BIGSERIAL PRIMARY KEY NOT NULL,
    peraturan_terkait       VARCHAR(255) NOT NULL UNIQUE,
	uraian                  VARCHAR(255) NOT NULL,
    kode_opd                VARCHAR(255),
    tahun                   INTEGER,
    created_date            timestamp NOT NULL default now(),
    last_modified_date      timestamp NOT NULL default now()
);
