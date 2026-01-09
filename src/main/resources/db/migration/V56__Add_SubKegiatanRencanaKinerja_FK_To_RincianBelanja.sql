ALTER TABLE rincian_belanja
ADD COLUMN id_subkegiatan_rencana_kinerja BIGINT;

ALTER TABLE rincian_belanja
ADD CONSTRAINT fk_rincian_belanja_subkegiatan_rencana_kinerja
FOREIGN KEY (id_subkegiatan_rencana_kinerja) REFERENCES subkegiatan_rencana_kinerja(id)
ON DELETE CASCADE;

ALTER TABLE rincian_belanja
DROP COLUMN id_subkegiatan;

ALTER TABLE rincian_belanja
ADD COLUMN id_rencana_aksi BIGINT;

ALTER TABLE rincian_belanja
ADD CONSTRAINT fk_rincian_belanja_rencana_aksi
FOREIGN KEY (id_rencana_aksi) REFERENCES rencana_aksi(id)
ON DELETE CASCADE;

ALTER TABLE rincian_belanja
RENAME COLUMN id_pegawai TO nip_pegawai;

ALTER TABLE rincian_belanja
ALTER COLUMN nip_pegawai TYPE VARCHAR(255);

ALTER TABLE rincian_belanja
ADD COLUMN created_date TIMESTAMP;

ALTER TABLE rincian_belanja
ADD COLUMN last_modified_date TIMESTAMP;
