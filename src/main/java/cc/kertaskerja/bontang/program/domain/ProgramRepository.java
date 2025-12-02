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

    @NonNull
    @Query("SELECT p.id, p.kode_program, p.nama_program, p.bidang_urusan_id, p.created_date, p.last_modified_date " +
            "FROM program p " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE b.kode_opd = :kodeOpd")
    List<Program> findAllByKodeOpd(@NonNull @Param("kodeOpd") String kodeOpd);

    @NonNull
    @Query("SELECT p.id, p.kode_program, p.nama_program, p.bidang_urusan_id, p.created_date, p.last_modified_date " +
            "FROM program p " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE p.kode_program = :kodeProgram AND b.kode_opd = :kodeOpd")
    Optional<Program> findByKodeProgramAndKodeOpd(
            @NonNull @Param("kodeProgram") String kodeProgram,
            @NonNull @Param("kodeOpd") String kodeOpd
    );

    @NonNull
    @Query("SELECT p.id, p.kode_program, p.nama_program, p.bidang_urusan_id, p.created_date, p.last_modified_date " +
            "FROM program p " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE p.kode_program IN (:kodePrograms) AND b.kode_opd = :kodeOpd")
    List<Program> findAllByKodeProgramInAndKodeOpd(
            @NonNull @Param("kodePrograms") Collection<String> kodePrograms,
            @NonNull @Param("kodeOpd") String kodeOpd
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM program WHERE kode_program = :kodeProgram")
    void deleteByKodeProgram(@NonNull @Param("kodeProgram") String kodeProgram);

    @Modifying
    @Transactional
    @Query("DELETE FROM program p USING bidang_urusan b " +
            "WHERE p.bidang_urusan_id = b.id " +
            "AND p.kode_program = :kodeProgram " +
            "AND b.kode_opd = :kodeOpd")
    void deleteByKodeProgramAndKodeOpd(
            @NonNull @Param("kodeProgram") String kodeProgram,
            @NonNull @Param("kodeOpd") String kodeOpd
    );
}
