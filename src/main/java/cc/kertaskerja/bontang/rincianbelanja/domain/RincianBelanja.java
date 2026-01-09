package cc.kertaskerja.bontang.rincianbelanja.domain;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rincian_belanja")
public record RincianBelanja(
    @Id
    Long id,
    
    @Column("id_subkegiatan_rencana_kinerja")
    Long idSubkegiatanRencanaKinerja,

    @Column("id_rencana_aksi")
    Long idRencanaAksi,
    
    @Column("nip_pegawai")
    String nipPegawai,
    
    @Column("nama_pegawai")
    String namaPegawai,
    
    @Column("kode_opd")
    String kodeOpd,
    
    @Column("nama_opd")
    String namaOpd,
    
    Integer tahun,
    
    @Column("kode_subkegiatan")
    String kodeSubkegiatan,
    
    @Column("nama_subkegiatan")
    String namaSubkegiatan,
    
    @Column("indikator")
    String indikator,
    
    @Column("target")
    String target,
    
    @Column("satuan")
    String satuan,
    
    @Column("sumber_dana")
    String sumberDana,
    
    @Column("rencana_aksi")
    String rencanaAksi,
    
    @Column("kode_rekening")
    String kodeRekening,
    
    @Column("nama_rekening")
    String namaRekening,
    
    Integer anggaran,
    
    @Column("total_anggaran")
    Integer totalAnggaran,
    
    @CreatedDate
    Instant createdDate,

    @LastModifiedDate
    Instant lastModifiedDate
) {
    public static RincianBelanja of (
        Long idSubkegiatanRencanaKinerja,
        Long idRencanaAksi,
        String nipPegawai,
        String namaPegawai,
        String kodeOpd,
        String namaOpd,
        Integer tahun,
        String kodeSubkegiatan,
        String namaSubkegiatan,
        String indikator,
        String target,
        String satuan,
        String sumberDana,
        String rencanaAksi,
        String kodeRekening,
        String namaRekening,
        Integer anggaran,
        Integer totalAnggaran
    ) {
        return new RincianBelanja(
            null,
            idSubkegiatanRencanaKinerja,
            idRencanaAksi,
            nipPegawai, 
            namaPegawai, 
            kodeOpd, 
            namaOpd, 
            tahun, 
            kodeSubkegiatan, 
            namaSubkegiatan, 
            indikator, 
            target, 
            satuan, 
            sumberDana, 
            rencanaAksi, 
            kodeRekening, 
            namaRekening, 
            anggaran, 
            totalAnggaran,
            null,
            null
        );
    }
}
