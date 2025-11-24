CREATE TABLE IF NOT EXISTS subkegiatan (
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    kode_subkegiatan   VARCHAR(255) NOT NULL UNIQUE,
    nama_subkegiatan   VARCHAR(255) NOT NULL,
    created_date       timestamp NOT NULL default now(),
    last_modified_date timestamp NOT NULL default now()
);
