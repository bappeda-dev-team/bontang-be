CREATE TABLE IF NOT EXISTS pelaksanaan_rencana_aksi (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
	id_rencana_aksi		VARCHAR(255) NOT NULL,
	bulan				INTEGER NOT NULL,
	bobot				INTEGER,
	bobot_tersedia		INTEGER,
	created_date		timestamp NOT NULL default now(),
	last_modified_date	timestamp NOT NULL default now()
);
