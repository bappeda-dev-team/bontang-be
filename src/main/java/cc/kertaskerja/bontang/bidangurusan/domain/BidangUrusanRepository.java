package cc.kertaskerja.bontang.bidangurusan.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

public interface BidangUrusanRepository extends CrudRepository<BidangUrusan, Long> {
    boolean existsByKodeOpdAndKodeBidangUrusan(@NonNull String kodeOpd, @NonNull String kodeBidangUrusan);

    Iterable<BidangUrusan> findByKodeOpd(@NonNull String kodeOpd);
}
