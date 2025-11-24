ALTER TABLE kegiatan DROP CONSTRAINT IF EXISTS kegiatan_kode_program_fkey;
ALTER TABLE kegiatan DROP COLUMN IF EXISTS kode_program;
