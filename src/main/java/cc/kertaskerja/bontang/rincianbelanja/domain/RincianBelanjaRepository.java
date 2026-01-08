
package cc.kertaskerja.bontang.rincianbelanja.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

public interface RincianBelanjaRepository extends CrudRepository<RincianBelanja, Long> {
    @NonNull
    Optional<RincianBelanja> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    List<RincianBelanja> findByNipPegawaiAndKodeOpdAndTahun(@NonNull String kodeOpd, @NonNull String nipPegawai, @NonNull Integer tahun);

    @NonNull
    Optional<RincianBelanja> findByIdAndNipPegawai(
            @NonNull @Param("id") Long id,
            @NonNull @Param("nipPegawai") String nipPegawai);

    @Modifying
    @Transactional
    @Query("DELETE FROM rincian_belanja WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);
}
