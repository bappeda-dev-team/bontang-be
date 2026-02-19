ALTER TABLE pegawai
    ADD COLUMN IF NOT EXISTS password_hash TEXT;
