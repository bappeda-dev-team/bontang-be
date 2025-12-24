package cc.kertaskerja.bontang.rencanakinerja.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface RencanaKinerjaRepository extends CrudRepository<RencanaKinerja, Long> {
    @NonNull
    Optional<RencanaKinerja> findById(@NonNull Long id);

    boolean existsById(@NonNull Long Id);

    @Modifying
    @Transactional
    @Query("DELETE FROM rencana_kinerja WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);

    List<RencanaKinerja> findByNipPegawaiAndKodeOpdAndTahun(
            @Param("nipPegawai") String nipPegawai,
            @Param("kodeOpd") String kodeOpd,
            @Param("tahun") Integer tahun);

    @NonNull
    Optional<RencanaKinerja> findByIdAndNipPegawai(
            @NonNull @Param("id") Long id,
            @NonNull @Param("nipPegawai") String nipPegawai);
}
