package cc.kertaskerja.bontang.rincianbelanja.web.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanja;

public record RincianBelanjaDetailResponse(
    Long id,
    Long idSumberDana,
    Long idRencanaKinerja,
    Long idRencanaAksi,
    OperasionalDaerahResponse operasionalDaerah,
    Integer tahun,
    String statusRincianBelanja,
    String nipPegawai,
    String namaPegawai,
    String sumberDana,
    String rencanaKinerja,
    String rencanaAksi,
    List<IndikatorDetailResponse> indikator,
    List<SubkegiatanResponse> subkegiatan,
    List<RincianAnggaranResponse> rincianAnggaran,
    List<TotalPerBulanResponse> totalPerBulan,
    Integer totalAnggaran
) {
    public static RincianBelanjaDetailResponse from(
        RincianBelanja rincianBelanja,
        List<IndikatorDetailResponse> indikatorList,
        List<SubkegiatanResponse> subkegiatanList,
        List<RincianAnggaranResponse> rincianAnggaranList,
        List<TotalPerBulanResponse> totalPerBulanList,
        int totalAnggaran
    ) {
        OperasionalDaerahResponse operasionalDaerah = OperasionalDaerahResponse.from(
            rincianBelanja.kodeOpd(),
            rincianBelanja.namaOpd()
        );

        return new RincianBelanjaDetailResponse(
            rincianBelanja.id(),
            rincianBelanja.idSumberDana(),
            rincianBelanja.idRencanaKinerja(),
            rincianBelanja.idRencanaAksi(),
            operasionalDaerah,
            rincianBelanja.tahun(),
            rincianBelanja.statusRincianBelanja(),
            rincianBelanja.nipPegawai(),
            rincianBelanja.namaPegawai(),
            rincianBelanja.sumberDana(),
            rincianBelanja.rencanaKinerja(),
            rincianBelanja.rencanaAksi(),
            indikatorList,
            subkegiatanList,
            rincianAnggaranList,
            totalPerBulanList,
            totalAnggaran
        );
    }

    // Convert to Map for backward compatibility
    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_rincian_belanja", id);
        map.put("id_sumber_dana", idSumberDana);
        map.put("id_rencana_kinerja", idRencanaKinerja);
        map.put("id_rencana_aksi", idRencanaAksi);
        map.put("tahun", tahun);
        map.put("statusRincianBelanja", statusRincianBelanja);
        map.put("nip_pegawai", nipPegawai);
        map.put("nama_pegawai", namaPegawai);

        Map<String, Object> operasionalDaerahMap = new LinkedHashMap<>();
        operasionalDaerahMap.put("kodeOpd", operasionalDaerah.kodeOpd());
        operasionalDaerahMap.put("namaOpd", operasionalDaerah.namaOpd());
        map.put("operasionalDaerah", operasionalDaerahMap);

        map.put("nip", nipPegawai);
        map.put("namaPegawai", namaPegawai);
        map.put("indikator", indikator.stream().map(this::indikatorToMap).toList());
        map.put("subKegiatan", subkegiatan.stream().map(this::subkegiatanToMap).toList());
        map.put("rincianAnggaran", rincianAnggaran.stream().map(this::rincianAnggaranToMap).toList());
        map.put("totalPerBulan", totalPerBulan.stream().map(this::totalPerBulanToMap).toList());
        map.put("totalAnggaran", totalAnggaran);

        return map;
    }

    private Map<String, Object> indikatorToMap(IndikatorDetailResponse indikator) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id_indikator", indikator.id());
        map.put("namaIndikator", indikator.namaIndikator());
        map.put("targets", indikator.targets().stream().map(t -> {
            Map<String, Object> targetMap = new LinkedHashMap<>();
            targetMap.put("id_target", t.id());
            targetMap.put("target", t.target());
            targetMap.put("satuan", t.satuan());
            return targetMap;
        }).toList());
        return map;
    }

    private Map<String, Object> subkegiatanToMap(SubkegiatanResponse subkegiatan) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", subkegiatan.id());
        map.put("rincian_belanja_id", subkegiatan.rincianBelanjaId());
        map.put("kodeSubKegiatan", subkegiatan.kodeSubKegiatan());
        map.put("namaSubKegiatan", subkegiatan.namaSubKegiatan());
        return map;
    }

    private Map<String, Object> rincianAnggaranToMap(RincianAnggaranResponse rincianAnggaran) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", rincianAnggaran.id());
        map.put("rincian_belanja_id", rincianAnggaran.rincianBelanjaId());
        map.put("urutan", rincianAnggaran.urutan());
        map.put("namaRincianAnggaran", rincianAnggaran.namaRincianBelanja());
        map.put("kodeRekening", rincianAnggaran.kodeRekening().stream().map(kodeRekening -> {
            Map<String, Object> kodeRekeningMap = new LinkedHashMap<>();
            kodeRekeningMap.put("idKodeRekening", kodeRekening.idKodeRekening());
            kodeRekeningMap.put("kodeRekening", kodeRekening.kodeRekening());
            kodeRekeningMap.put("namaRekening", kodeRekening.namaRekening());
            return kodeRekeningMap;
        }).toList());
        map.put("pelaksanaan", rincianAnggaran.pelaksanaan());
        map.put("jumlahBobot", rincianAnggaran.jumlahBobot());
        return map;
    }

    private Map<String, Object> totalPerBulanToMap(TotalPerBulanResponse total) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("bulan", total.bulan());
        map.put("totalBobot", total.totalBobot());
        return map;
    }
}
