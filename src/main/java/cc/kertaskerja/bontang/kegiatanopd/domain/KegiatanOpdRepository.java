package cc.kertaskerja.bontang.kegiatanopd.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface KegiatanOpdRepository extends CrudRepository<KegiatanOpd, Long> {
	@NonNull
    Optional<KegiatanOpd> findById(@NonNull Long id);

    boolean existsByKodeKegiatanOpd(@NonNull String kodeKegiatanOpd);

    @NonNull
    Optional<KegiatanOpd> findByKodeKegiatanOpd(@NonNull String kodeKegiatanOpd);

    @Modifying
    @Transactional
    @Query("DELETE FROM kegiatan_opd WHERE kode_kegiatan = :kodeKegiatanOpd")
    void deleteByKodeKegiatanOpd(@NonNull @Param("kodeKegiatanOpd") String kodeKegiatanOpd);
}