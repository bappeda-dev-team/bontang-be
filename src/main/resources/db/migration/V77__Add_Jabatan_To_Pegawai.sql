ALTER TABLE pegawai
    ADD COLUMN jabatan_id BIGINT;

ALTER TABLE pegawai
    ADD CONSTRAINT fk_pegawai_jabatan
    FOREIGN KEY (jabatan_id) REFERENCES jabatan(id)
    ON DELETE CASCADE;
