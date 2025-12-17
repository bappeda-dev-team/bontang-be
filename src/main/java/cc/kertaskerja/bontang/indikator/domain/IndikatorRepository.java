package cc.kertaskerja.bontang.indikator.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

public interface IndikatorRepository extends CrudRepository<Indikator, Long> {
	@NonNull
    Optional<Indikator> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM indikator WHERE id = :id")
    void deleteIndikatorById(@NonNull @Param("id") Long id);
}