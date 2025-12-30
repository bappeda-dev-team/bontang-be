package cc.kertaskerja.bontang.subkegiatanopd.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

public interface SubKegiatanOpdRepository extends CrudRepository<SubKegiatanOpd, Long> {
	@NonNull
    Optional<SubKegiatanOpd> findById(@NonNull Long id);

    boolean existsByKodeSubKegiatanOpd(@NonNull String kodeSubKegiatanOpd);

    @NonNull
    Optional<SubKegiatanOpd> findByKodeSubKegiatanOpd(@NonNull String kodeSubKegiatanOpd);
    
    @NonNull
    Iterable<SubKegiatanOpd> findByKodeOpdAndTahun(@NonNull String kodeOpd, @NonNull Integer tahun);

    @Modifying
    @Transactional
    @Query("DELETE FROM subkegiatan_opd WHERE kode_subkegiatan = :kodeSubKegiatanOpd")
    void deleteByKodeSubKegiatanOpd(@NonNull @Param("kodeSubKegiatanOpd") String kodeSubKegiatanOpd);
}
