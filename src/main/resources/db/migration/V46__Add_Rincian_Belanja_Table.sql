CREATE TABLE IF NOT EXISTS rincian_belanja (
	id		                BIGSERIAL PRIMARY KEY NOT NULL,
    id_sumber_dana          INTEGER,
    id_rencana_kinerja      INTEGER,
    id_rencana_aksi         INTEGER,
    kode_opd                VARCHAR(255),
    nama_opd                VARCHAR(255),
    tahun                   INTEGER,
    status_rincian_belanja  VARCHAR(255),
    nip_pegawai             VARCHAR(255),
    nama_pegawai            VARCHAR(255),
    sumber_dana             VARCHAR(255) NOT NULL,
    rencana_kinerja         VARCHAR(255) NOT NULL,
    rencana_aksi            VARCHAR(255) NOT NULL,
    total_anggaran          INTEGER,
    created_date            timestamp NOT NULL default now(),
    last_modified_date      timestamp NOT NULL default now()
);
