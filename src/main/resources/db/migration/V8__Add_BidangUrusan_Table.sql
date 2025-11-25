CREATE TABLE IF NOT EXISTS bidang_urusan (
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    kode_opd            VARCHAR(255) NOT NULL,
    kode_bidang_urusan  VARCHAR(255) NOT NULL,
    nama_bidang_urusan  VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now(),
    CONSTRAINT fk_bidang_urusan_opd FOREIGN KEY (kode_opd) REFERENCES opd(kode_opd),
    CONSTRAINT uq_bidang_urusan_per_opd UNIQUE (kode_opd, kode_bidang_urusan)
);
