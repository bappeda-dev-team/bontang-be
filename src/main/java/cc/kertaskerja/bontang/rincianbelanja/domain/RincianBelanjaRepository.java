package cc.kertaskerja.bontang.rincianbelanja.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface RincianBelanjaRepository extends CrudRepository<RincianBelanja, Long> {
    @NonNull
    Optional<RincianBelanja> findById(Long id);

    List<RincianBelanja> findByIdSubkegiatanRencanaKinerja(@NonNull Long idSubkegiatanRencanaKinerja);

    Optional<RincianBelanja> findByIdRencanaAksi(@NonNull Long idRencanaAksi);

    List<RincianBelanja> findByNipPegawaiAndKodeOpdAndTahun(@NonNull String nipPegawai, String kodeOpd, @NonNull Integer tahun);

    List<RincianBelanja> findByKodeOpdAndTahun(String kodeOpd, @NonNull Integer tahun);

    @Modifying
    @Transactional
    @Query("DELETE FROM rincian_belanja WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);

}
