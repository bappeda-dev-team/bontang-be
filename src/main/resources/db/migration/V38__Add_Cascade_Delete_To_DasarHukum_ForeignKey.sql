ALTER TABLE dasar_hukum DROP CONSTRAINT fk_dasar_hukum_rencana_kinerja;

ALTER TABLE dasar_hukum ADD CONSTRAINT fk_dasar_hukum_rencana_kinerja
    FOREIGN KEY (id_rencana_kinerja) REFERENCES rencana_kinerja(id) ON DELETE CASCADE;
