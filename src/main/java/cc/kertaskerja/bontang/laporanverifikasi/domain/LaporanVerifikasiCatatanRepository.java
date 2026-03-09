package cc.kertaskerja.bontang.laporanverifikasi.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface LaporanVerifikasiCatatanRepository extends CrudRepository<LaporanVerifikasiCatatan, Long> {
    @NonNull
    Optional<LaporanVerifikasiCatatan> findByJenisLaporanAndKodeOpdAndTahunAndFilterHashAndTahapVerifikasi(
            @NonNull String jenisLaporan,
            @NonNull String kodeOpd,
            @NonNull Integer tahun,
            @NonNull String filterHash,
            @NonNull String tahapVerifikasi
    );
}
