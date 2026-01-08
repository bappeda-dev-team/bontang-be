CREATE TABLE IF NOT EXISTS indikator_belanja (
	id		                    BIGSERIAL PRIMARY KEY NOT NULL,
    id_rincian_belanja          BIGINT NOT NULL,
    nama_indikator_belanja      VARCHAR(255),
    created_date                timestamp NOT NULL default now(),
    last_modified_date          timestamp NOT NULL default now(),
    CONSTRAINT fk_indikator_belanja_rincian_belanja FOREIGN KEY (id_rincian_belanja) REFERENCES rincian_belanja(id) ON DELETE CASCADE
);
