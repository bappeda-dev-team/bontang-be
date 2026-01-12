ALTER TABLE subkegiatan_rencana_kinerja DROP CONSTRAINT IF EXISTS subkegiatan_rencana_kinerja_id_rekin_fkey;

ALTER TABLE subkegiatan_rencana_kinerja 
ADD CONSTRAINT subkegiatan_rencana_kinerja_id_rekin_fkey 
FOREIGN KEY (id_rekin) REFERENCES rencana_kinerja(id) ON DELETE CASCADE;
