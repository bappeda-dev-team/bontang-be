package cc.kertaskerja.bontang.programopd.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface ProgramOpdRepository extends CrudRepository<ProgramOpd, Long> {
	@NonNull
    Optional<ProgramOpd> findById(@NonNull Long id);

    boolean existsByKodeProgramOpd(@NonNull String kodeProgramOpd);

    @NonNull
    Optional<ProgramOpd> findByKodeProgramOpd(@NonNull String kodeProgramOpd);

    Iterable<ProgramOpd> findByKodeOpdAndTahun(@NonNull String kodeOpd, @NonNull Integer tahun);

    @Modifying
    @Transactional
    @Query("DELETE FROM program_opd WHERE kode_program = :kodeProgramOpd")
    void deleteByKodeProgramOpd(@NonNull @Param("kodeProgramOpd") String kodeProgramOpd);
}