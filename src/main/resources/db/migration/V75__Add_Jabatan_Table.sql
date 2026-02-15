CREATE TABLE IF NOT EXISTS jabatan (
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    nama_jabatan       VARCHAR(255) NOT NULL,
    kode_jabatan       VARCHAR(255) NOT NULL,
    jenis_jabatan      VARCHAR(255) NOT NULL,
    kode_opd           VARCHAR(255),
    created_date       timestamp     NOT NULL default now(),
    last_modified_date timestamp     NOT NULL default now()
);
