package cc.kertaskerja.bontang.jabatan.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JabatanRepository extends CrudRepository<Jabatan, Long> {
    @NonNull
    Optional<Jabatan> findById(@NonNull Long id);

    boolean existsByKodeJabatan(@NonNull String kodeJabatan);

    @NonNull
    Optional<Jabatan> findByKodeJabatan(@NonNull String kodeJabatan);

    @NonNull
    Optional<Jabatan> findByNamaJabatan(@NonNull String namaJabatan);

    Iterable<Jabatan> findByKodeOpd(@NonNull String kodeOpd);

    @Modifying
    @Transactional
    @Query("DELETE FROM jabatan WHERE kode_jabatan = :kodeJabatan")
    void deleteByKodeJabatan(@NonNull @Param("kodeJabatan") String kodeJabatan);
}
