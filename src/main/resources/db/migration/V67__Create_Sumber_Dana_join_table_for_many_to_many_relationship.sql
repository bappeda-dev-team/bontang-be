CREATE TABLE IF NOT EXISTS rencana_kinerja_sumber_dana (
    id BIGSERIAL PRIMARY KEY,
    id_rencana_kinerja BIGINT NOT NULL,
    id_sumber_dana BIGINT NOT NULL,
    FOREIGN KEY (id_rencana_kinerja) REFERENCES rencana_kinerja(id) ON DELETE CASCADE,
    FOREIGN KEY (id_sumber_dana) REFERENCES sumber_dana(id) ON DELETE CASCADE,
    UNIQUE(id_rencana_kinerja, id_sumber_dana)
);
