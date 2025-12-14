package cc.kertaskerja.bontang.bidangurusan.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface BidangUrusanRepository extends CrudRepository<BidangUrusan, Long> {
    boolean existsByKodeOpdAndKodeBidangUrusan(@NonNull String kodeOpd, @NonNull String kodeBidangUrusan);

    @NonNull
    Optional<BidangUrusan> findByKodeBidangUrusan(@NonNull String kodeBidangUrusan);

    boolean existsByKodeOpd(@NonNull String kodeOpd);

    Iterable<BidangUrusan> findByKodeOpd(@NonNull String kodeOpd);
}