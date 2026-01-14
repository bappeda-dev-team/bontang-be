ALTER TABLE program_prioritas
    ADD COLUMN id_sub_kegiatan_opd BIGINT,
    ADD COLUMN tahun INTEGER NOT NULL,
    ADD COLUMN keterangan VARCHAR(255),
    ADD COLUMN periode_tahun_awal INTEGER,
    ADD COLUMN periode_tahun_akhir INTEGER,
    ADD COLUMN status VARCHAR(50),
    ADD COLUMN kode_opd VARCHAR(50),
    ADD COLUMN kode_sub_kegiatan_opd VARCHAR(50);

ALTER TABLE program_prioritas
    ADD CONSTRAINT fk_program_prioritas_subkegiatan_opd
        FOREIGN KEY (id_sub_kegiatan_opd)
        REFERENCES subkegiatan_opd (id);
