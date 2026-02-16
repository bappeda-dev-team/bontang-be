ALTER TABLE pegawai
    DROP CONSTRAINT IF EXISTS fk_pegawai_jabatan;

ALTER TABLE pegawai
    ADD CONSTRAINT fk_pegawai_jabatan
    FOREIGN KEY (jabatan_id) REFERENCES jabatan(id)
    ON DELETE SET NULL;
