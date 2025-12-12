ALTER TABLE program DROP COLUMN IF EXISTS bidang_urusan_id;
ALTER TABLE kegiatan DROP COLUMN IF EXISTS program_id;
ALTER TABLE subkegiatan DROP COLUMN IF EXISTS kegiatan_id;
