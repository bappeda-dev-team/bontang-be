ALTER TABLE rencana_kinerja
    DROP COLUMN IF EXISTS indikator,
    DROP COLUMN IF EXISTS target,
    DROP COLUMN IF EXISTS id_rekin,
    DROP COLUMN IF EXISTS bulan;

ALTER TABLE rencana_kinerja
    RENAME COLUMN id_pegawai TO nip;