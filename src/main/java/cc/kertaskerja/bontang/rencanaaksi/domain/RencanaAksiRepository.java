package cc.kertaskerja.bontang.rencanaaksi.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface RencanaAksiRepository extends CrudRepository<RencanaAksi, Long> {
    @NonNull
    Optional<RencanaAksi> findById(@NonNull Long id);

    boolean existsById(@NonNull Long Id);

    @Modifying
    @Transactional
    @Query("DELETE FROM rencana_aksi WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);
}
