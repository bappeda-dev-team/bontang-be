ALTER TABLE program_prioritas
    DROP CONSTRAINT fk_program_prioritas_subkegiatan_opd;

ALTER TABLE program_prioritas
    ADD CONSTRAINT fk_program_prioritas_subkegiatan_opd
        FOREIGN KEY (id_sub_kegiatan_opd)
        REFERENCES subkegiatan_opd (id)
        ON DELETE CASCADE;
