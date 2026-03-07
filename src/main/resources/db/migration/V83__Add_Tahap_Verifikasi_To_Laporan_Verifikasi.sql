ALTER TABLE laporan_verifikasi
    ADD COLUMN IF NOT EXISTS tahap_verifikasi VARCHAR(50) NOT NULL DEFAULT 'LEVEL_2';

ALTER TABLE laporan_verifikasi
    DROP CONSTRAINT IF EXISTS uq_laporan_verifikasi_scope;

ALTER TABLE laporan_verifikasi
    ADD CONSTRAINT uq_laporan_verifikasi_scope
        UNIQUE (jenis_laporan, kode_opd, tahun, filter_hash, tahap_verifikasi);

DROP INDEX IF EXISTS idx_laporan_verifikasi_scope;

CREATE INDEX IF NOT EXISTS idx_laporan_verifikasi_scope
    ON laporan_verifikasi(jenis_laporan, kode_opd, tahun, filter_hash, tahap_verifikasi);
