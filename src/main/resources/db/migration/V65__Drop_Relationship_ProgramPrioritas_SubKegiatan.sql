ALTER TABLE program_prioritas DROP CONSTRAINT IF EXISTS fk_program_prioritas_subkegiatan;

ALTER TABLE program_prioritas DROP COLUMN IF EXISTS id_subkegiatan;
