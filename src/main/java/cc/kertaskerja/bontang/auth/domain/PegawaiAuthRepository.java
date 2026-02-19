package cc.kertaskerja.bontang.auth.domain;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

public interface PegawaiAuthRepository extends Repository<PegawaiAuthView, String> {

    @NonNull
    @Query("SELECT nip, password_hash, role FROM pegawai WHERE nip = :nip")
    Optional<PegawaiAuthView> findAuthByNip(@NonNull @Param("nip") String nip);

    @Query("SELECT EXISTS(SELECT 1 FROM pegawai WHERE password_hash IS NOT NULL)")
    boolean anyPasswordHashSet();

    @Modifying
    @Transactional
    @Query("UPDATE pegawai SET password_hash = :passwordHash, role = :role WHERE nip = :nip")
    int setPasswordHashAndRole(
            @NonNull @Param("nip") String nip,
            @NonNull @Param("passwordHash") String passwordHash,
            @NonNull @Param("role") String role
    );
}

