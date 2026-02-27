CREATE TABLE IF NOT EXISTS laporan_verifikasi (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    jenis_laporan VARCHAR(100) NOT NULL,
    kode_opd VARCHAR(255) NOT NULL,
    tahun INTEGER NOT NULL,
    filter_hash VARCHAR(255) NOT NULL DEFAULT '-',
    verified_by_nip VARCHAR(255) NOT NULL,
    verified_at TIMESTAMP NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT NOW(),
    last_modified_date TIMESTAMP NULL,
    CONSTRAINT fk_laporan_verifikasi_pegawai
        FOREIGN KEY (verified_by_nip) REFERENCES pegawai(nip),
    CONSTRAINT uq_laporan_verifikasi_scope UNIQUE (jenis_laporan, kode_opd, tahun, filter_hash)
);

CREATE INDEX IF NOT EXISTS idx_laporan_verifikasi_scope
    ON laporan_verifikasi(jenis_laporan, kode_opd, tahun, filter_hash);

CREATE INDEX IF NOT EXISTS idx_laporan_verifikasi_verified_by_nip
    ON laporan_verifikasi(verified_by_nip);
