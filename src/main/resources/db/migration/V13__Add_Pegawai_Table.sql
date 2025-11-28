CREATE TABLE IF NOT EXISTS pegawai (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
    opd_id              BIGINT NOT NULL,
	nama_pegawai        VARCHAR(255) NOT NULL,
    nip                 VARCHAR(255) NOT NULL UNIQUE,
    email               VARCHAR(255) NOT NULL,
    jabatan_dinas       VARCHAR(255) NOT NULL,
    jabatan_tim         VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now(),
    CONSTRAINT fk_pegawai_opd FOREIGN KEY (opd_id) REFERENCES opd(id)
);
