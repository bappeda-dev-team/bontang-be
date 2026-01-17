CREATE TABLE IF NOT EXISTS program_prioritas_opd (
    id                          BIGSERIAL PRIMARY KEY NOT NULL,
    id_sub_kegiatan_opd         BIGINT,
    id_rencana_kinerja          BIGINT,
    nama_program_prioritas      VARCHAR(255) NOT NULL,
    tahun                       INTEGER NOT NULL,
    kode_opd                    VARCHAR(255),
    status                      VARCHAR(255),
    keterangan                  VARCHAR(255),
    created_date                timestamp NOT NULL default now(),
    last_modified_date          timestamp NOT NULL default now()
);

-- Add foreign key to subkegiatan_opd
ALTER TABLE program_prioritas_opd
    ADD CONSTRAINT fk_program_prioritas_opd_subkegiatan_opd
        FOREIGN KEY (id_sub_kegiatan_opd)
        REFERENCES subkegiatan_opd (id) ON DELETE SET NULL;

-- Add foreign key to rencana_kinerja
ALTER TABLE program_prioritas_opd
    ADD CONSTRAINT fk_program_prioritas_opd_rencana_kinerja
        FOREIGN KEY (id_rencana_kinerja)
        REFERENCES rencana_kinerja (id) ON DELETE SET NULL;
