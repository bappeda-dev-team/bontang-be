ALTER TABLE rencana_kinerja
ADD COLUMN id_sumber_dana BIGINT;

ALTER TABLE rencana_kinerja
ADD CONSTRAINT fk_rencana_kinerja_sumber_dana
FOREIGN KEY (id_sumber_dana) REFERENCES sumber_dana(id) ON DELETE CASCADE;
