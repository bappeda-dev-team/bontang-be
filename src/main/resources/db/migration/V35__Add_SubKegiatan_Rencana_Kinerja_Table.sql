CREATE TABLE IF NOT EXISTS subkegiatan_rencana_kinerja (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    id_rekin INT NOT NULL,
    kode_subkegiatan VARCHAR(255) NOT NULL,
    nama_subkegiatan VARCHAR(255) NOT NULL,
    created_date timestamp NOT NULL default now(),
    last_modified_date timestamp NOT NULL default now(),
    FOREIGN KEY (id_rekin) REFERENCES rencana_kinerja(id)
);