package cc.kertaskerja.bontang.programprioritas.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProgramPrioritasRepository extends CrudRepository<ProgramPrioritas, Long> {
    @NonNull
    Optional<ProgramPrioritas> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    Iterable<ProgramPrioritas> findByPeriodeTahunAwalGreaterThanEqualAndPeriodeTahunAkhirLessThanEqual(
            Integer periodeTahunAwal,
            Integer periodeTahunAkhir
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM program_prioritas WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);
}
