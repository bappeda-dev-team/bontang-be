ALTER TABLE rencana_aksi
    RENAME COLUMN rencana_aksi TO nama_rencana_aksi;

ALTER TABLE rencana_aksi
    DROP COLUMN IF EXISTS id_pegawai,
    DROP COLUMN IF EXISTS bulan,
    DROP COLUMN IF EXISTS tahun,
    DROP COLUMN IF EXISTS created_by;
