package cc.kertaskerja.bontang.targetbelanja.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

public interface TargetBelanjaRepository extends CrudRepository<TargetBelanja, Long> {
    @NonNull
    Optional<TargetBelanja> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    List<TargetBelanja> findByIndikatorBelanjaId(Long indikatorBelanjaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM target_belanja WHERE id = :id")
    void deleteTargerBelanjaById(@NonNull @Param("id") Long id);
}
