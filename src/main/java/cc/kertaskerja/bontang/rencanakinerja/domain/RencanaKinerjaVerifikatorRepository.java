package cc.kertaskerja.bontang.rencanakinerja.domain;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface RencanaKinerjaVerifikatorRepository extends CrudRepository<RencanaKinerjaVerifikator, Long> {
    @NonNull
    List<RencanaKinerjaVerifikator> findByIdRencanaKinerja(@NonNull Long idRencanaKinerja);

    @NonNull
    List<RencanaKinerjaVerifikator> findByNipVerifikator(@NonNull String nipVerifikator);

    @Modifying
    @Transactional
    @Query("DELETE FROM rencana_kinerja_verifikator WHERE id_rencana_kinerja = :idRencanaKinerja")
    void deleteByIdRencanaKinerja(@NonNull @Param("idRencanaKinerja") Long idRencanaKinerja);
}
