ALTER TABLE rencana_aksi
    ADD CONSTRAINT fk_rencana_aksi_rencana_kinerja
    FOREIGN KEY (id_rekin)
    REFERENCES rencana_kinerja(id)
    ON DELETE CASCADE;
