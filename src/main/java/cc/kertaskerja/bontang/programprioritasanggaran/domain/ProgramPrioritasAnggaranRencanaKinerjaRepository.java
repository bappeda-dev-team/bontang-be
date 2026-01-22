package cc.kertaskerja.bontang.programprioritasanggaran.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProgramPrioritasAnggaranRencanaKinerjaRepository extends CrudRepository<ProgramPrioritasAnggaranRencanaKinerja, Long> {
    List<ProgramPrioritasAnggaranRencanaKinerja> findByIdProgramPrioritasAnggaran(Long idProgramPrioritasAnggaran);

    Optional<ProgramPrioritasAnggaranRencanaKinerja> findByIdProgramPrioritasAnggaranAndIdRencanaKinerja(
            Long idProgramPrioritasAnggaran,
            Long idRencanaKinerja
    );

    boolean existsByIdProgramPrioritasAnggaranAndIdRencanaKinerja(
            Long idProgramPrioritasAnggaran,
            Long idRencanaKinerja
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM program_prioritas_anggaran_rencana_kinerja WHERE id_program_prioritas_anggaran = :idProgramPrioritasAnggaran")
    void deleteByIdProgramPrioritasAnggaran(@NonNull @Param("idProgramPrioritasAnggaran") Long idProgramPrioritasAnggaran);

    @Modifying
    @Transactional
    @Query("DELETE FROM program_prioritas_anggaran_rencana_kinerja WHERE id_program_prioritas_anggaran = :idProgramPrioritasAnggaran AND id_rencana_kinerja = :idRencanaKinerja")
    void deleteByIdProgramPrioritasAnggaranAndIdRencanaKinerja(
            @NonNull @Param("idProgramPrioritasAnggaran") Long idProgramPrioritasAnggaran,
            @NonNull @Param("idRencanaKinerja") Long idRencanaKinerja
    );
}
