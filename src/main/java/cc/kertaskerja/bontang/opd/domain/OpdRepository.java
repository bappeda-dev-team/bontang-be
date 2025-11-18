package cc.kertaskerja.bontang.opd.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OpdRepository extends CrudRepository<Opd, Long> {
    @NonNull
    Optional<Opd> findById(@NonNull Long id);

    boolean existsByKodeOpd(@NonNull String kodeOpd);

    @NonNull
    Optional<Opd> findByKodeOpd(@NonNull String kodeOpd);

    @Modifying
    @Transactional
    @Query("DELETE FROM opd WHERE kode_opd = :kodeOpd")
    void deleteByKodeOpd(@NonNull String kodeOpd);
}
