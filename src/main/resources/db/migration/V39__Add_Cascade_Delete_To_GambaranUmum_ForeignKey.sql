ALTER TABLE gambaran_umum DROP CONSTRAINT fk_gambaran_umum_rencana_kinerja;

ALTER TABLE gambaran_umum ADD CONSTRAINT fk_gambaran_umum_rencana_kinerja
    FOREIGN KEY (id_rencana_kinerja) REFERENCES rencana_kinerja(id) ON DELETE CASCADE;
