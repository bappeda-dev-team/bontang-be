package cc.kertaskerja.bontang.programprioritasopd.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProgramPrioritasOpdRepository extends CrudRepository<ProgramPrioritasOpd, Long> {
    @NonNull
    Optional<ProgramPrioritasOpd> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);

    Iterable<ProgramPrioritasOpd> findByIdProgramPrioritas(Long idProgramPrioritas);
    
    Iterable<ProgramPrioritasOpd> findByIdSubkegiatanOpd(Long idSubkegiatanOpd);

    Iterable<ProgramPrioritasOpd> findByIdRencanaKinerja(Long idRencanaKinerja);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM program_prioritas_opd WHERE id = :id")
    void deleteById(@NonNull @Param("id") Long id);
}
