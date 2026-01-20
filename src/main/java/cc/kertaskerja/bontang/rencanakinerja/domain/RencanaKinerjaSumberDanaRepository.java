package cc.kertaskerja.bontang.rencanakinerja.domain;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface RencanaKinerjaSumberDanaRepository extends CrudRepository<RencanaKinerjaSumberDana, Long> {
    @NonNull
    List<RencanaKinerjaSumberDana> findByIdRencanaKinerja(@NonNull Long idRencanaKinerja);

    @Modifying
    @Transactional
    @Query("DELETE FROM rencana_kinerja_sumber_dana WHERE id_rencana_kinerja = :idRencanaKinerja")
    void deleteByIdRencanaKinerja(@NonNull @Param("idRencanaKinerja") Long idRencanaKinerja);
}
