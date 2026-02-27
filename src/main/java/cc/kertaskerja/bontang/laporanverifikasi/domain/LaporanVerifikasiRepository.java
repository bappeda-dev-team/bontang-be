package cc.kertaskerja.bontang.laporanverifikasi.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface LaporanVerifikasiRepository extends CrudRepository<LaporanVerifikasi, Long> {
    @NonNull
    Optional<LaporanVerifikasi> findByJenisLaporanAndKodeOpdAndTahunAndFilterHash(
            @NonNull String jenisLaporan,
            @NonNull String kodeOpd,
            @NonNull Integer tahun,
            @NonNull String filterHash
    );
}
