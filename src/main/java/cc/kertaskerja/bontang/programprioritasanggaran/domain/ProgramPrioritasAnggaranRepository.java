package cc.kertaskerja.bontang.programprioritasanggaran.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProgramPrioritasAnggaranRepository extends CrudRepository<ProgramPrioritasAnggaran, Long> {
    @NonNull
    Optional<ProgramPrioritasAnggaran> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    Optional<ProgramPrioritasAnggaran> findByIdProgramPrioritasAndKodeOpd(
            Long idProgramPrioritas,
            String kodeOpd
    );

    Iterable<ProgramPrioritasAnggaran> findByIdProgramPrioritas(Long idProgramPrioritas);

    Iterable<ProgramPrioritasAnggaran> findByKodeOpd(String kodeOpd);

    Iterable<ProgramPrioritasAnggaran> findByKodeOpdAndNipAndTahun(
            String kodeOpd,
            String nip,
            Integer tahun
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM program_prioritas_anggaran WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);
}
