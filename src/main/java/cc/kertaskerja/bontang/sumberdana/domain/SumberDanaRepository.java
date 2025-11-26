package cc.kertaskerja.bontang.sumberdana.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SumberDanaRepository extends CrudRepository<SumberDana, Long> {
    @NonNull
    Optional<SumberDana> findById(@NonNull Long id);

    boolean existsById(@NonNull Long Id);

    @Modifying
    @Transactional
    @Query("DELETE FROM sumber_dana WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);
}
