package cc.kertaskerja.bontang.kegiatan.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface KegiatanRepository extends CrudRepository<Kegiatan, Long> {
    @NonNull
    Optional<Kegiatan> findById(@NonNull Long id);

    boolean existsByKodeKegiatan(@NonNull String kodeKegiatan);

    boolean existsByProgramId(@NonNull Long programId);

    @NonNull
    Optional<Kegiatan> findByKodeKegiatan(@NonNull String kodeKegiatan);

    @Modifying
    @Transactional
    @Query("DELETE FROM kegiatan WHERE kode_kegiatan = :kodeKegiatan")
    void deleteByKodeKegiatan(@NonNull @Param("kodeKegiatan") String kodeKegiatan);
}
