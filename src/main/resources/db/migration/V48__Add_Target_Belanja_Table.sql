CREATE TABLE IF NOT EXISTS target_belanja (
	id		                    BIGSERIAL PRIMARY KEY NOT NULL,
    id_indikator_belanja        BIGINT NOT NULL,
    target_belanja              VARCHAR(255),
    satuan                      VARCHAR(255),
    created_date                timestamp NOT NULL default now(),
    last_modified_date          timestamp NOT NULL default now(),
    CONSTRAINT fk_target_belanja_indikator_belanja FOREIGN KEY (id_indikator_belanja) REFERENCES indikator_belanja(id) ON DELETE CASCADE
);
