package cc.kertaskerja.bontang.target.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TargetRepository extends CrudRepository<Target, Long> {
    Optional<Target> findFirstByTarget(String target);
    
    List<Target> findByIndikatorId(Long indikatorId);
}
