package cc.kertaskerja.bontang.pegawai.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PegawaiRepository extends CrudRepository<Pegawai, Long> {
    @NonNull
    Optional<Pegawai> findById(@NonNull Long id);

    boolean existsByNip(@NonNull String nip);

    @NonNull
    Optional<Pegawai> findByNip(@NonNull String nip);
    
    Iterable<Pegawai> findByKodeOpdAndTahun(@NonNull String kodeOpd, @NonNull Integer tahun);

    @Modifying
    @Transactional
    @Query("DELETE FROM pegawai WHERE nip = :nip")
    void deleteByNip(@NonNull @Param("nip") String nip);

}
