-- Create rincian_belanja table
CREATE TABLE rincian_belanja (
    id BIGSERIAL PRIMARY KEY,
    id_subkegiatan INTEGER,
    id_pegawai INTEGER,
    nama_pegawai VARCHAR(255),
    kode_opd VARCHAR(50),
    nama_opd VARCHAR(255),
    tahun INTEGER,
    kode_subkegiatan VARCHAR(255),
    nama_subkegiatan VARCHAR(255),
    indikator VARCHAR(255),
    target VARCHAR(255),
    satuan VARCHAR(20),
    sumber_dana VARCHAR(255),
    rencana_aksi VARCHAR(255),
    kode_rekening VARCHAR(255),
    nama_rekening VARCHAR(255),
    anggaran INTEGER,
    total_anggaran INTEGER
);

ALTER TABLE rincian_belanja
ADD CONSTRAINT fk_rincian_belanja_subkegiatan
FOREIGN KEY (id_subkegiatan) REFERENCES subkegiatan(id)
ON DELETE CASCADE;
