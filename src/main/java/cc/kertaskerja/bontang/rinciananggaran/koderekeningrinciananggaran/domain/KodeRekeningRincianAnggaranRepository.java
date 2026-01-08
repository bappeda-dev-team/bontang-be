package cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface KodeRekeningRincianAnggaranRepository extends CrudRepository<KodeRekeningRincianAnggaran, Long> {
    @NonNull
    Optional<KodeRekeningRincianAnggaran> findById(@NonNull Long id);

    List<KodeRekeningRincianAnggaran> findByIdRincianAnggaran(Long idRincianAnggaran);

    @Modifying
    @Transactional
    @Query("DELETE FROM kode_rekening_rincian_anggaran WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);
}
