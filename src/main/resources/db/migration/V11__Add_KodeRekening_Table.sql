CREATE TABLE IF NOT EXISTS kode_rekening (
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    kode_rekening       VARCHAR(255) NOT NULL,
    nama_rekening       VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);