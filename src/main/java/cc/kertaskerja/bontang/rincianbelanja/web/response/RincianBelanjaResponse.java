package cc.kertaskerja.bontang.rincianbelanja.web.response;

import java.util.List;

public record RincianBelanjaResponse(
    String nipPegawai,
    String namaPegawai,
    String kodeSubkegiatan,
    String namaSubkegiatan,
    Integer totalAnggaran,
    List<RincianBelanjaItem> rincianBelanja
) {
    public record RincianBelanjaItem(
        String idRencanaKinerja,
        String rencanaKinerja,
        List<Indikator> indikator,
        Integer totalAnggaran,
        List<RencanaAksi> rencanaAksi
    ) {}

    public record Indikator(
        String idIndikator,
        String namaIndikator,
        List<Target> targets
    ) {}

    public record Target(
        String idTarget,
        String target,
        String satuan
    ) {}

    public record RencanaAksi(
        String idRencanaAksi,
        String renaksi,
        Integer anggaran,
        String kodeRekening,
        String namaRekening
    ) {}
}
