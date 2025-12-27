package cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

public interface SubKegiatanRencanaKinerjaRepository extends CrudRepository<SubKegiatanRencanaKinerja, Long> {
    @NonNull
    Optional<SubKegiatanRencanaKinerja> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    List<SubKegiatanRencanaKinerja> findByIdRekin(Integer idRekin);

    Optional<SubKegiatanRencanaKinerja> findByIdRekinAndId(Integer idRekin, Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM subkegiatan_rencana_kinerja WHERE id = :id")
    void deleteSubKegiatanRencanaKinerjaById(@NonNull @Param("id") Long id);
}
