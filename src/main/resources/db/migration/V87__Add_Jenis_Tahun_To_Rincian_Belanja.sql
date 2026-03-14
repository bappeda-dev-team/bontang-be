ALTER TABLE rincian_belanja
ADD COLUMN jenis_tahun VARCHAR(255);

UPDATE rincian_belanja rb
SET jenis_tahun = rk.jenis_tahun
FROM rencana_aksi ra
JOIN rencana_kinerja rk ON rk.id = ra.id_rekin
WHERE rb.id_rencana_aksi = ra.id;
