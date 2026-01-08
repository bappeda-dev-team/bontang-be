package cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PelaksanaanRincianAnggaranRepository extends CrudRepository<PelaksanaanRincianAnggaran, Long> {
    @NonNull
    Optional<PelaksanaanRincianAnggaran> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM pelaksanaan_rincian_anggaran WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM pelaksanaan_rincian_anggaran WHERE id_rincian_anggaran = :idRincianAnggaran")
    void deleteByIdRincianAnggaran(@NonNull @Param("idRincianAnggaran") Integer idRincianAnggaran);

    @Query("SELECT COALESCE(SUM(bobot), 0) FROM pelaksanaan_rincian_anggaran WHERE id_rincian_anggaran = :idRincianAnggaran")
    Integer sumBobotByIdRincianAnggaran(@Param("idRincianAnggaran") Integer idRincianAnggaran);

    @Query("SELECT COALESCE(SUM(bobot), 0) FROM pelaksanaan_rincian_anggaran WHERE id_rincian_anggaran = :idRincianAnggaran AND id != :excludeId")
    Integer sumBobotByIdRincianAnggaranExcluding(@Param("idRincianAnggaran") Integer idRincianAnggaran,
                                                 @Param("excludeId") Long excludeId);

    @Query("SELECT * FROM pelaksanaan_rincian_anggaran WHERE id_rincian_anggaran = :idRincianAnggaran LIMIT 1")
    Optional<PelaksanaanRincianAnggaran> findByIdRincianAnggaran(@Param("idRincianAnggaran") Integer idRincianAnggaran);

    @Query("SELECT * FROM pelaksanaan_rincian_anggaran WHERE id_rincian_anggaran = :idRincianAnggaran ORDER BY bulan")
    Iterable<PelaksanaanRincianAnggaran> findByIdRincianAnggaranOrderByBulan(@Param("idRincianAnggaran") Integer idRincianAnggaran);
}