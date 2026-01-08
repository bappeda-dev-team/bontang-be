package cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.common.lang.NonNull;

public interface SubKegiatanRincianBelanjaRepository extends CrudRepository<SubKegiatanRincianBelanja, Long> {
    @NonNull
    Optional<SubKegiatanRincianBelanja> findById(@NonNull Long id);

    Optional<SubKegiatanRincianBelanja> findByKodeSubKegiatanRincianBelanja(String kodeSubKegiatanRincianBelanja);

    List<SubKegiatanRincianBelanja> findByIdRincianBelanja(Integer idRincianBelanja);

    Optional<SubKegiatanRincianBelanja> findByIdRincianBelanjaAndKodeSubKegiatanRincianBelanja(Integer idRincianBelanja, String kodeSubKegiatanRincianBelanja);

    @Modifying
    @Transactional
    @Query("DELETE FROM subkegiatan_rincian_belanja WHERE kode_subkegiatan = :kodeSubKegiatanRincianBelanja")
    void deleteByKodeSubKegiatanRincianBelanja(@NonNull @Param("kodeSubKegiatanRincianBelanja") String kodeSubKegiatanRincianBelanja);
}
