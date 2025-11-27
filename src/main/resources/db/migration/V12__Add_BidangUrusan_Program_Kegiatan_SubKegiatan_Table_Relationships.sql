ALTER TABLE program ADD COLUMN IF NOT EXISTS bidang_urusan_id BIGINT;
ALTER TABLE program DROP CONSTRAINT IF EXISTS fk_program_bidang_urusan;
ALTER TABLE program ADD CONSTRAINT fk_program_bidang_urusan FOREIGN KEY (bidang_urusan_id) REFERENCES bidang_urusan(id);

ALTER TABLE kegiatan ADD COLUMN IF NOT EXISTS program_id BIGINT;
ALTER TABLE kegiatan DROP CONSTRAINT IF EXISTS fk_kegiatan_program;
ALTER TABLE kegiatan ADD CONSTRAINT fk_kegiatan_program FOREIGN KEY (program_id) REFERENCES program(id);

ALTER TABLE subkegiatan ADD COLUMN IF NOT EXISTS kegiatan_id BIGINT;
ALTER TABLE subkegiatan DROP CONSTRAINT IF EXISTS fk_subkegiatan_kegiatan;
ALTER TABLE subkegiatan ADD CONSTRAINT fk_subkegiatan_kegiatan FOREIGN KEY (kegiatan_id) REFERENCES kegiatan(id);
