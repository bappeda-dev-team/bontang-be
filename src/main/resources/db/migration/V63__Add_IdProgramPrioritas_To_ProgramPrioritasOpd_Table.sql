-- Add column id_program_prioritas to program_prioritas_opd table
ALTER TABLE program_prioritas_opd
    ADD COLUMN id_program_prioritas BIGINT;

-- Add foreign key to program_prioritas
ALTER TABLE program_prioritas_opd
    ADD CONSTRAINT fk_program_prioritas_opd_program_prioritas
        FOREIGN KEY (id_program_prioritas)
        REFERENCES program_prioritas (id) ON DELETE SET NULL;
