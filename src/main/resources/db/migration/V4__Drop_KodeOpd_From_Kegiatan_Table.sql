-- Remove redundant OPD link from kegiatan; rely on program -> opd chain
ALTER TABLE kegiatan DROP CONSTRAINT IF EXISTS kegiatan_kode_opd_fkey;
ALTER TABLE kegiatan DROP COLUMN IF EXISTS kode_opd;
