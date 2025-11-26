CREATE TABLE IF NOT EXISTS sumber_dana (
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    kode_dana_lama      VARCHAR(255) NOT NULL,
    sumber_dana         VARCHAR(255) NOT NULL,
    kode_dana_baru      VARCHAR(255) NOT NULL,
    set_input           VARCHAR(20) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);
