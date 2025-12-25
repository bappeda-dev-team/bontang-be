-- Add foreign key column id_rencana_kinerja to dasar_hukum table
ALTER TABLE dasar_hukum ADD COLUMN id_rencana_kinerja BIGINT;

-- Add foreign key constraint
ALTER TABLE dasar_hukum ADD CONSTRAINT fk_dasar_hukum_rencana_kinerja
    FOREIGN KEY (id_rencana_kinerja) REFERENCES rencana_kinerja(id);
