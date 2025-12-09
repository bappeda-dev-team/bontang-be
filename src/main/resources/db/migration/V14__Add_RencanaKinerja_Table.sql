CREATE TABLE IF NOT EXISTS rencana_kinerja (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
    rencana_kinerja     VARCHAR(255) NOT NULL,
	indikator           VARCHAR(255) NOT NULL,
    target              VARCHAR(255) NOT NULL UNIQUE,
    sumber_dana         VARCHAR(255) NOT NULL,
    keterangan          VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);
