ALTER TABLE program_prioritas_anggaran
ADD COLUMN nip VARCHAR(255),
ADD COLUMN tahun INTEGER;

CREATE INDEX idx_program_prioritas_anggaran_nip ON program_prioritas_anggaran(nip);

CREATE INDEX idx_program_prioritas_anggaran_tahun ON program_prioritas_anggaran(tahun);
