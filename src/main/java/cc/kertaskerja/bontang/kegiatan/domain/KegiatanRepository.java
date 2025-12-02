package cc.kertaskerja.bontang.kegiatan.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface KegiatanRepository extends CrudRepository<Kegiatan, Long> {
    @NonNull
    Optional<Kegiatan> findById(@NonNull Long id);

    boolean existsByKodeKegiatan(@NonNull String kodeKegiatan);

    boolean existsByProgramId(@NonNull Long programId);

    @NonNull
    @Query("SELECT k.id, k.kode_kegiatan, k.nama_kegiatan, k.program_id, k.created_date, k.last_modified_date " +
            "FROM kegiatan k " +
            "JOIN program p ON k.program_id = p.id " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE b.kode_opd = :kodeOpd")
    List<Kegiatan> findAllByKodeOpd(@NonNull @Param("kodeOpd") String kodeOpd);

    @NonNull
    Optional<Kegiatan> findByKodeKegiatan(@NonNull String kodeKegiatan);

    @NonNull
    @Query("SELECT k.id, k.kode_kegiatan, k.nama_kegiatan, k.program_id, k.created_date, k.last_modified_date " +
            "FROM kegiatan k " +
            "JOIN program p ON k.program_id = p.id " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE k.kode_kegiatan = :kodeKegiatan AND b.kode_opd = :kodeOpd")
    Optional<Kegiatan> findByKodeKegiatanAndKodeOpd(
            @NonNull @Param("kodeKegiatan") String kodeKegiatan,
            @NonNull @Param("kodeOpd") String kodeOpd
    );

    @NonNull
    List<Kegiatan> findAllByKodeKegiatanIn(@NonNull Collection<String> kodeKegiatans);

    @NonNull
    @Query("SELECT k.id, k.kode_kegiatan, k.nama_kegiatan, k.program_id, k.created_date, k.last_modified_date " +
            "FROM kegiatan k " +
            "JOIN program p ON k.program_id = p.id " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE k.kode_kegiatan IN (:kodeKegiatans) " +
            "AND b.kode_opd = :kodeOpd")
    List<Kegiatan> findAllByKodeKegiatanInAndKodeOpd(
            @NonNull @Param("kodeKegiatans") Collection<String> kodeKegiatans,
            @NonNull @Param("kodeOpd") String kodeOpd
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM kegiatan WHERE kode_kegiatan = :kodeKegiatan")
    void deleteByKodeKegiatan(@NonNull @Param("kodeKegiatan") String kodeKegiatan);

    @Modifying
    @Transactional
    @Query("DELETE FROM kegiatan k USING program p, bidang_urusan b " +
            "WHERE k.program_id = p.id " +
            "AND p.bidang_urusan_id = b.id " +
            "AND k.kode_kegiatan = :kodeKegiatan " +
            "AND b.kode_opd = :kodeOpd")
    void deleteByKodeKegiatanAndKodeOpd(
            @NonNull @Param("kodeKegiatan") String kodeKegiatan,
            @NonNull @Param("kodeOpd") String kodeOpd
    );
}
