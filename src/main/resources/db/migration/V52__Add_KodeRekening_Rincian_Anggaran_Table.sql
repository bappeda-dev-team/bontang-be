CREATE TABLE IF NOT EXISTS kode_rekening_rincian_anggaran (
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    id_rincian_anggaran BIGINT NOT NULL,
    id_kode_rekening    BIGINT NOT NULL,
    kode_rekening       VARCHAR(255) NOT NULL,
    nama_rekening       VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now(),
    CONSTRAINT fk_pelaksanaan_rincian_anggaran_rincian_anggaran FOREIGN KEY (id_rincian_anggaran) REFERENCES rincian_anggaran(id) ON DELETE CASCADE,
    CONSTRAINT fk_kode_rekening_rincian_anggaran_kode_rekening FOREIGN KEY (id_kode_rekening) REFERENCES kode_rekening(id) ON DELETE CASCADE
);
