package cc.kertaskerja.bontang.rinciananggaran.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface RincianAnggaranRepository extends CrudRepository<RincianAnggaran, Long> {
    @NonNull
    Optional<RincianAnggaran> findById(@NonNull Long id);

    boolean existsById(@NonNull Long Id);

    @Modifying
    @Transactional
    @Query("DELETE FROM rincian_anggaran WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);

    @Query("SELECT * FROM rincian_anggaran WHERE id_rincian_belanja = :idRincianBelanja ORDER BY urutan")
    Iterable<RincianAnggaran> findByIdRincianAnggaranOrderByUrutan(@Param("idRincianBelanja") Integer idRincianBelanja);
}
