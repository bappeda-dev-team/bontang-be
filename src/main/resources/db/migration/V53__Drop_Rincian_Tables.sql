-- Drop foreign key constraints first
ALTER TABLE kode_rekening_rincian_anggaran DROP CONSTRAINT IF EXISTS fk_pelaksanaan_rincian_anggaran_rincian_anggaran;

-- Drop tables in correct order (child tables first, then parent tables)
DROP TABLE IF EXISTS pelaksanaan_rincian_anggaran CASCADE;
DROP TABLE IF EXISTS kode_rekening_rincian_anggaran CASCADE;
DROP TABLE IF EXISTS rincian_anggaran CASCADE;
DROP TABLE IF EXISTS subkegiatan_rincian_belanja CASCADE;
DROP TABLE IF EXISTS indikator_belanja CASCADE;
DROP TABLE IF EXISTS rincian_belanja CASCADE;