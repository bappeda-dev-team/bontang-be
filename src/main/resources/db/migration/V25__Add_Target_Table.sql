CREATE TABLE IF NOT EXISTS target (
	id		            BIGSERIAL PRIMARY KEY NOT NULL,
    target              VARCHAR(255) NOT NULL,
    satuan              VARCHAR(255) NOT NULL,
    created_date        timestamp NOT NULL default now(),
    last_modified_date  timestamp NOT NULL default now()
);
