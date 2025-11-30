package cc.kertaskerja.bontang.program.domain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface ProgramRepository extends CrudRepository<Program, Long> {
    @NonNull
    Optional<Program> findById(@NonNull Long id);

    boolean existsByKodeProgram(@NonNull String kodeProgram);

    boolean existsByBidangUrusanId(@NonNull Long bidangUrusanId);

    @NonNull
    Optional<Program> findByKodeProgram(@NonNull String kodeProgram);

    @NonNull
    List<Program> findAllByKodeProgramIn(@NonNull Collection<String> kodePrograms);

    @Modifying
    @Transactional
    @Query("DELETE FROM program WHERE kode_program = :kodeProgram")
    void deleteByKodeProgram(@NonNull @Param("kodeProgram") String kodeProgram);
}
