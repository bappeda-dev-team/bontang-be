CREATE TABLE IF NOT EXISTS program (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
	kode_program        VARCHAR(255) NOT NULL UNIQUE,
    nama_program        VARCHAR(255) NOT NULL,
    kode_opd            VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now(),
    CONSTRAINT fk_program_opd FOREIGN KEY (kode_opd) REFERENCES opd(kode_opd)
);
