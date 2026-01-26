package cc.kertaskerja.bontang.laporanprogramprioritas.domain;

import cc.kertaskerja.bontang.programprioritas.domain.ProgramPrioritas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface LaporanProgramPrioritasRepository extends CrudRepository<ProgramPrioritas, Long> {
    @NonNull
    Optional<ProgramPrioritas> findById(@NonNull Long id);

    boolean existsById(@NonNull Long id);
}
