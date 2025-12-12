ALTER TABLE subkegiatan DROP CONSTRAINT IF EXISTS fk_subkegiatan_kegiatan;

ALTER TABLE kegiatan DROP CONSTRAINT IF EXISTS fk_kegiatan_program;
ALTER TABLE kegiatan DROP CONSTRAINT IF EXISTS kegiatan_kode_opd_fkey;

ALTER TABLE program DROP CONSTRAINT IF EXISTS fk_program_bidang_urusan;
ALTER TABLE program DROP CONSTRAINT IF EXISTS fk_program_opd;

ALTER TABLE bidang_urusan DROP CONSTRAINT IF EXISTS fk_bidang_urusan_opd;
