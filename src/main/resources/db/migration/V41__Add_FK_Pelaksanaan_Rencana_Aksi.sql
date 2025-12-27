ALTER TABLE pelaksanaan_rencana_aksi
    ADD CONSTRAINT fk_pelaksanaan_rencana_aksi_rencana_aksi
    FOREIGN KEY (id_rencana_aksi)
    REFERENCES rencana_aksi(id)
    ON DELETE CASCADE;
