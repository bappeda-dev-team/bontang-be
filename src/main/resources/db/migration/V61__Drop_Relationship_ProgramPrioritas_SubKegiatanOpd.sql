-- Hapus relasi antara tabel program_prioritas dan subkegiatan_opd
ALTER TABLE program_prioritas DROP CONSTRAINT IF EXISTS fk_program_prioritas_subkegiatan_opd;

ALTER TABLE program_prioritas DROP COLUMN IF EXISTS id_sub_kegiatan_opd;
ALTER TABLE program_prioritas DROP COLUMN IF EXISTS kode_sub_kegiatan_opd;
ALTER TABLE program_prioritas DROP COLUMN IF EXISTS kode_opd;

-- Tambah relasi antara tabel program_prioritas dengan tabel subkegiatan via id_subkegiatan
ALTER TABLE program_prioritas
    ADD COLUMN id_subkegiatan BIGINT;

ALTER TABLE program_prioritas
    ADD CONSTRAINT fk_program_prioritas_subkegiatan
        FOREIGN KEY (id_subkegiatan)
        REFERENCES subkegiatan (id) ON DELETE CASCADE;
