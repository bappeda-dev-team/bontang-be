-- Change id_rencana_aksi column from VARCHAR to INTEGER in pelaksanaan_rencana_aksi table
-- For PostgreSQL database

ALTER TABLE pelaksanaan_rencana_aksi
ALTER COLUMN id_rencana_aksi TYPE INTEGER USING id_rencana_aksi::INTEGER;