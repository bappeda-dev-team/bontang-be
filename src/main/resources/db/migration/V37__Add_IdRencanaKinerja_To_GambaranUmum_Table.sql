ALTER TABLE gambaran_umum ADD COLUMN id_rencana_kinerja BIGINT;

ALTER TABLE gambaran_umum ADD CONSTRAINT fk_gambaran_umum_rencana_kinerja
    FOREIGN KEY (id_rencana_kinerja) REFERENCES rencana_kinerja(id);
