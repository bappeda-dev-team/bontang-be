package cc.kertaskerja.bontang.subkegiatan.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SubKegiatanRepository extends CrudRepository<SubKegiatan, Long> {
    @NonNull
    Optional<SubKegiatan> findById(@NonNull Long id);

    boolean existsByKodeSubKegiatan(@NonNull String kodeSubKegiatan);

    @NonNull
    Optional<SubKegiatan> findByKodeSubKegiatan(@NonNull String kodeSubKegiatan);

    @NonNull
    List<SubKegiatan> findAllByKodeSubKegiatanIn(@NonNull Collection<String> kodeSubKegiatans);

    Iterable<SubKegiatan> findByKodeOpd(@NonNull String kodeOpd);

    List<SubKegiatan> findByKodeSubKegiatanStartingWith(@NonNull String prefix);

    @Modifying
    @Transactional
    @Query("DELETE FROM subkegiatan WHERE kode_subkegiatan = :kodeSubKegiatan")
    void deleteByKodeSubKegiatan(@NonNull @Param("kodeSubKegiatan") String kodeSubKegiatan);
}
