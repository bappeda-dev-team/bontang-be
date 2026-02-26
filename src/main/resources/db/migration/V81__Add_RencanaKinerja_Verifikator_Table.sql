CREATE TABLE IF NOT EXISTS rencana_kinerja_verifikator (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    id_rencana_kinerja BIGINT NOT NULL,
    nip_verifikator VARCHAR(255) NOT NULL,
    CONSTRAINT fk_rencana_kinerja_verifikator_rekin
        FOREIGN KEY (id_rencana_kinerja) REFERENCES rencana_kinerja(id) ON DELETE CASCADE,
    CONSTRAINT fk_rencana_kinerja_verifikator_pegawai
        FOREIGN KEY (nip_verifikator) REFERENCES pegawai(nip),
    CONSTRAINT uq_rencana_kinerja_verifikator UNIQUE (id_rencana_kinerja, nip_verifikator)
);

CREATE INDEX IF NOT EXISTS idx_rkv_rencana_kinerja ON rencana_kinerja_verifikator(id_rencana_kinerja);
CREATE INDEX IF NOT EXISTS idx_rkv_nip_verifikator ON rencana_kinerja_verifikator(nip_verifikator);
