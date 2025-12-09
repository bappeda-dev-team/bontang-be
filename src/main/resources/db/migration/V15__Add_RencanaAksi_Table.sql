CREATE TABLE IF NOT EXISTS rencana_aksi (
	id					BIGSERIAL PRIMARY KEY NOT NULL,
    rencana_aksi        VARCHAR(255) NOT NULL,
    urutan              INTEGER NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);
