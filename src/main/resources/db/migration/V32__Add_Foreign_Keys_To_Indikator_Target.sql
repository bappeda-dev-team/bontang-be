ALTER TABLE indikator 
ADD COLUMN rencana_kinerja_id BIGINT NOT NULL;
  
ALTER TABLE target
ADD COLUMN indikator_id BIGINT NOT NULL;

ALTER TABLE indikator 
ADD CONSTRAINT fk_indikator_rencana_kinerja 
FOREIGN KEY (rencana_kinerja_id) REFERENCES rencana_kinerja(id) 
ON DELETE CASCADE;

ALTER TABLE target 
ADD CONSTRAINT fk_target_indikator 
FOREIGN KEY (indikator_id) REFERENCES indikator(id) 
ON DELETE CASCADE;
