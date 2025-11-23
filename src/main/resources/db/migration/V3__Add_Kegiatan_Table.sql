CREATE TABLE IF NOT EXISTS kegiatan (
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    kode_kegiatan       VARCHAR(255) NOT NULL UNIQUE,
    nama_kegiatan       VARCHAR(255) NOT NULL,
    kode_opd            VARCHAR(255) NOT NULL,
    kode_program        VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now(),
    FOREIGN KEY (kode_opd) REFERENCES opd(kode_opd),
    FOREIGN KEY (kode_program) REFERENCES program(kode_program)
);
