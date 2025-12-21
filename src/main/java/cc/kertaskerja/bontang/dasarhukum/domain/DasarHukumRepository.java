package cc.kertaskerja.bontang.dasarhukum.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

public interface DasarHukumRepository extends CrudRepository<DasarHukum, Long> {
	@NonNull
    Optional<DasarHukum> findById(@NonNull Long id);

    boolean existsById(@NonNull String id);

    @Modifying
    @Transactional
    @Query("DELETE FROM dasar_hukum WHERE id = :id")
    void deleteByKodeOpd(@NonNull @Param("id") String id);
    
    Iterable<DasarHukum> findByKodeOpd(String kodeOpd);
}