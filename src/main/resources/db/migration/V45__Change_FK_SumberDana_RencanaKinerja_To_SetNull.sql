ALTER TABLE rencana_kinerja
DROP CONSTRAINT fk_rencana_kinerja_sumber_dana;

ALTER TABLE rencana_kinerja
ALTER COLUMN id_sumber_dana DROP NOT NULL;

ALTER TABLE rencana_kinerja
ALTER COLUMN sumber_dana DROP NOT NULL;

ALTER TABLE rencana_kinerja
ADD CONSTRAINT fk_rencana_kinerja_sumber_dana
FOREIGN KEY (id_sumber_dana) REFERENCES sumber_dana(id) ON DELETE SET NULL;
