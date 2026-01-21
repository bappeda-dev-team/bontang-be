CREATE TABLE IF NOT EXISTS program_prioritas_anggaran (
    id                          BIGSERIAL PRIMARY KEY NOT NULL,
    id_program_prioritas        BIGINT NOT NULL,
    kode_opd                    VARCHAR(255) NOT NULL,
    created_date                timestamp NOT NULL DEFAULT now(),
    last_modified_date          timestamp NOT NULL DEFAULT now(),
    CONSTRAINT fk_program_prioritas_anggaran_program_prioritas
        FOREIGN KEY (id_program_prioritas)
        REFERENCES program_prioritas (id) ON DELETE CASCADE
);

CREATE INDEX idx_program_prioritas_anggaran_pp ON program_prioritas_anggaran(id_program_prioritas);
CREATE INDEX idx_program_prioritas_anggaran_opd ON program_prioritas_anggaran(kode_opd);

CREATE TABLE IF NOT EXISTS program_prioritas_anggaran_rencana_kinerja (
    id                          BIGSERIAL PRIMARY KEY NOT NULL,
    id_program_prioritas_anggaran BIGINT NOT NULL,
    id_rencana_kinerja          BIGINT NOT NULL,
    created_date                timestamp NOT NULL DEFAULT now(),
    last_modified_date          timestamp NOT NULL DEFAULT now(),
    CONSTRAINT fk_ppark_perencanaanggaran
        FOREIGN KEY (id_program_prioritas_anggaran)
        REFERENCES program_prioritas_anggaran (id) ON DELETE CASCADE,
    CONSTRAINT fk_ppark_rencanakinerja
        FOREIGN KEY (id_rencana_kinerja)
        REFERENCES rencana_kinerja (id) ON DELETE CASCADE,
    CONSTRAINT uq_ppark_unique UNIQUE(id_program_prioritas_anggaran, id_rencana_kinerja)
);

CREATE INDEX idx_ppark_program_prioritas_anggaran ON program_prioritas_anggaran_rencana_kinerja(id_program_prioritas_anggaran);
CREATE INDEX idx_ppark_rencana_kinerja ON program_prioritas_anggaran_rencana_kinerja(id_rencana_kinerja);
