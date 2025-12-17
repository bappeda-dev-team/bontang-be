CREATE TABLE IF NOT EXISTS indikator (
	id		            BIGSERIAL PRIMARY KEY NOT NULL,
    nama_indikator      VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);
