package cc.kertaskerja.bontang.subkegiatan.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SubKegiatanRepository extends CrudRepository<SubKegiatan, Long> {
    @NonNull
    Optional<SubKegiatan> findById(@NonNull Long id);

    boolean existsByKodeSubKegiatan(@NonNull String kodeSubKegiatan);

    boolean existsByKegiatanId(@NonNull Long kegiatanId);

    @NonNull
    Optional<SubKegiatan> findByKodeSubKegiatan(@NonNull String kodeSubKegiatan);

    @NonNull
    @Query("SELECT s.id, s.kode_subkegiatan, s.nama_subkegiatan, s.kegiatan_id, s.created_date, s.last_modified_date " +
            "FROM subkegiatan s " +
            "JOIN kegiatan k ON s.kegiatan_id = k.id " +
            "JOIN program p ON k.program_id = p.id " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE b.kode_opd = :kodeOpd")
    List<SubKegiatan> findAllByKodeOpd(@NonNull @Param("kodeOpd") String kodeOpd);

    @NonNull
    List<SubKegiatan> findAllByKodeSubKegiatanIn(@NonNull Collection<String> kodeSubKegiatans);

    @NonNull
    @Query("SELECT s.id, s.kode_subkegiatan, s.nama_subkegiatan, s.kegiatan_id, s.created_date, s.last_modified_date " +
            "FROM subkegiatan s " +
            "JOIN kegiatan k ON s.kegiatan_id = k.id " +
            "JOIN program p ON k.program_id = p.id " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE s.kode_subkegiatan IN (:kodeSubKegiatans) " +
            "AND b.kode_opd = :kodeOpd")
    List<SubKegiatan> findAllByKodeSubKegiatanInAndKodeOpd(
            @NonNull @Param("kodeSubKegiatans") Collection<String> kodeSubKegiatans,
            @NonNull @Param("kodeOpd") String kodeOpd
    );

    @NonNull
    @Query("SELECT s.id, s.kode_subkegiatan, s.nama_subkegiatan, s.kegiatan_id, s.created_date, s.last_modified_date " +
            "FROM subkegiatan s " +
            "JOIN kegiatan k ON s.kegiatan_id = k.id " +
            "JOIN program p ON k.program_id = p.id " +
            "JOIN bidang_urusan b ON p.bidang_urusan_id = b.id " +
            "WHERE s.kode_subkegiatan = :kodeSubKegiatan " +
            "AND b.kode_opd = :kodeOpd")
    Optional<SubKegiatan> findByKodeSubKegiatanAndKodeOpd(
            @NonNull @Param("kodeSubKegiatan") String kodeSubKegiatan,
            @NonNull @Param("kodeOpd") String kodeOpd
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM subkegiatan WHERE kode_subkegiatan = :kodeSubKegiatan")
    void deleteByKodeSubKegiatan(@NonNull @Param("kodeSubKegiatan") String kodeSubKegiatan);
}
