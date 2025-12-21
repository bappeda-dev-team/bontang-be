package cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PelaksanaanRepository extends CrudRepository<Pelaksanaan, Long> {
    @NonNull
    Optional<Pelaksanaan> findById(@NonNull Long id);

    boolean existsById(@NonNull Long Id);

    @Modifying
    @Transactional
    @Query("DELETE FROM pelaksanaan_rencana_aksi WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM pelaksanaan_rencana_aksi WHERE id_rencana_aksi = :idRencanaAksi")
    void deleteByIdRencanaAksi(@NonNull @Param("idRencanaAksi") Integer idRencanaAksi);

    @Query("SELECT COALESCE(SUM(bobot), 0) FROM pelaksanaan_rencana_aksi WHERE id_rencana_aksi = :idRencanaAksi")
    Integer sumBobotByIdRencanaAksi(@Param("idRencanaAksi") Integer idRencanaAksi);

    @Query("SELECT COALESCE(SUM(bobot), 0) FROM pelaksanaan_rencana_aksi WHERE id_rencana_aksi = :idRencanaAksi AND id != :excludeId")
    Integer sumBobotByIdRencanaAksiExcluding(@Param("idRencanaAksi") Integer idRencanaAksi,
                                               @Param("excludeId") Long excludeId);

    @Query("SELECT * FROM pelaksanaan_rencana_aksi WHERE id_rencana_aksi = :idRencanaAksi LIMIT 1")
    Optional<Pelaksanaan> findByIdRencanaAksi(@Param("idRencanaAksi") Integer idRencanaAksi);
}
