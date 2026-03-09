CREATE TABLE IF NOT EXISTS laporan_verifikasi_catatan (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    jenis_laporan VARCHAR(100) NOT NULL,
    kode_opd VARCHAR(255) NOT NULL,
    tahun INTEGER NOT NULL,
    filter_hash VARCHAR(255) NOT NULL DEFAULT '-',
    tahap_verifikasi VARCHAR(50) NOT NULL,
    catatan VARCHAR(255) NOT NULL,
    catatan_by_nip VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT NOW(),
    last_modified_date TIMESTAMP NULL,
    CONSTRAINT fk_laporan_verifikasi_catatan_pegawai
        FOREIGN KEY (catatan_by_nip) REFERENCES pegawai(nip),
    CONSTRAINT uq_laporan_verifikasi_catatan_scope
        UNIQUE (jenis_laporan, kode_opd, tahun, filter_hash, tahap_verifikasi)
);

CREATE INDEX IF NOT EXISTS idx_laporan_verifikasi_catatan_scope
    ON laporan_verifikasi_catatan(jenis_laporan, kode_opd, tahun, filter_hash, tahap_verifikasi);

CREATE INDEX IF NOT EXISTS idx_laporan_verifikasi_catatan_by_nip
    ON laporan_verifikasi_catatan(catatan_by_nip);
