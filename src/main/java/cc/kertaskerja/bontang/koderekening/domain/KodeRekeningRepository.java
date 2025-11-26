package cc.kertaskerja.bontang.koderekening.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface KodeRekeningRepository extends CrudRepository<KodeRekening, Long> {
    @NonNull
    Optional<KodeRekening> findById(@NonNull Long id);

    boolean existsByKodeRekening(@NonNull String kodeRekening);

    @NonNull
    Optional<KodeRekening> findByKodeRekening(@NonNull String kodeRekening);

    @Modifying
    @Transactional
    @Query("DELETE FROM kode_rekening WHERE kode_rekening = :kodeRekening")
    void deleteByKodeRekening(@NonNull @Param("kodeRekening") String kodeRekening);
}
