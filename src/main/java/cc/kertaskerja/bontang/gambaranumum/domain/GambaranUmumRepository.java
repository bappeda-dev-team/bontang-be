package cc.kertaskerja.bontang.gambaranumum.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

public interface GambaranUmumRepository extends CrudRepository<GambaranUmum, Long> {
	@NonNull
    Optional<GambaranUmum> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM gambaran_umum WHERE kode_opd = :kodeOpd")
    void deleteByKodeOpd(@NonNull @Param("kodeOpd") String kodeOpd);
}