package cc.kertaskerja.bontang.laporanrincianbelanja.domain;

import cc.kertaskerja.bontang.indikator.domain.Indikator;
import cc.kertaskerja.bontang.indikator.domain.IndikatorRepository;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaEnvelopeResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaIndikatorResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaRencanaAksiResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaRencanaKinerjaResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaSubkegiatanResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaTargetResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaAllOpdResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaOpdResponse;
import cc.kertaskerja.bontang.opd.domain.Opd;
import cc.kertaskerja.bontang.opd.domain.OpdRepository;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksiRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaVerifikator;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaVerifikatorRepository;
import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanja;
import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanjaRepository;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerjaRepository;
import cc.kertaskerja.bontang.target.domain.Target;
import cc.kertaskerja.bontang.target.domain.TargetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LaporanRincianBelanjaService {
    private final RencanaKinerjaRepository rencanaKinerjaRepository;
    private final SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository;
    private final IndikatorRepository indikatorRepository;
    private final TargetRepository targetRepository;
    private final RencanaAksiRepository rencanaAksiRepository;
    private final RincianBelanjaRepository rincianBelanjaRepository;
    private final RencanaKinerjaVerifikatorRepository rencanaKinerjaVerifikatorRepository;
    private final OpdRepository opdRepository;

    public LaporanRincianBelanjaService(
            RencanaKinerjaRepository rencanaKinerjaRepository,
            SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository,
            IndikatorRepository indikatorRepository,
            TargetRepository targetRepository,
            RencanaAksiRepository rencanaAksiRepository,
            RincianBelanjaRepository rincianBelanjaRepository,
            RencanaKinerjaVerifikatorRepository rencanaKinerjaVerifikatorRepository,
            OpdRepository opdRepository
    ) {
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.subKegiatanRencanaKinerjaRepository = subKegiatanRencanaKinerjaRepository;
        this.indikatorRepository = indikatorRepository;
        this.targetRepository = targetRepository;
        this.rencanaAksiRepository = rencanaAksiRepository;
        this.rincianBelanjaRepository = rincianBelanjaRepository;
        this.rencanaKinerjaVerifikatorRepository = rencanaKinerjaVerifikatorRepository;
        this.opdRepository = opdRepository;
    }

    public LaporanRincianBelanjaEnvelopeResponse getLaporanRincianBelanja(
            String kodeOpd,
            Integer tahun,
            String requesterNip,
            boolean isLevel2
    ) {
        List<RencanaKinerja> rencanaKinerjaList =
                rencanaKinerjaRepository.findByKodeOpdAndTahun(kodeOpd, tahun);

        if (isLevel2) {
            Set<Long> allowedRencanaKinerjaIds = rencanaKinerjaVerifikatorRepository.findByNipVerifikator(requesterNip).stream()
                    .map(relasi -> relasi.idRencanaKinerja())
                    .collect(Collectors.toSet());
            rencanaKinerjaList = rencanaKinerjaList.stream()
                    .filter(rencanaKinerja -> allowedRencanaKinerjaIds.contains(rencanaKinerja.id()))
                    .toList();
        }

        if (rencanaKinerjaList.isEmpty()) {
            return new LaporanRincianBelanjaEnvelopeResponse(
                    200,
                    "success get laporan rincian belanja",
                    List.of()
            );
        }

        return buildLaporanRincianBelanja(rencanaKinerjaList, kodeOpd, tahun);
    }

    public LaporanRincianBelanjaEnvelopeResponse getLaporanRincianBelanjaByPegawai(
            String kodeOpd,
            Integer tahun,
            String nipPegawai
    ) {
        List<RencanaKinerja> rencanaKinerjaList =
                rencanaKinerjaRepository.findByKodeOpdAndTahun(kodeOpd, tahun);

        rencanaKinerjaList = rencanaKinerjaList.stream()
                .filter(rencanaKinerja -> Objects.equals(rencanaKinerja.nipPegawai(), nipPegawai))
                .toList();

        if (rencanaKinerjaList.isEmpty()) {
            return new LaporanRincianBelanjaEnvelopeResponse(
                    200,
                    "success get laporan rincian belanja",
                    List.of()
            );
        }

        return buildLaporanRincianBelanja(rencanaKinerjaList, kodeOpd, tahun);
    }

    public LaporanRincianBelanjaAllOpdResponse getLaporanRincianBelanjaAllOpd(
            Integer tahun,
            String requesterNip,
            boolean isLevel2
    ) {
        List<RencanaKinerja> rencanaKinerjaList = rencanaKinerjaRepository.findByTahun(tahun);

        if (isLevel2) {
            Set<Long> allowedRencanaKinerjaIds = rencanaKinerjaVerifikatorRepository.findByNipVerifikator(requesterNip).stream()
                    .map(relasi -> relasi.idRencanaKinerja())
                    .collect(Collectors.toSet());
            rencanaKinerjaList = rencanaKinerjaList.stream()
                    .filter(rencanaKinerja -> allowedRencanaKinerjaIds.contains(rencanaKinerja.id()))
                    .toList();
        }

        if (rencanaKinerjaList.isEmpty()) {
            return new LaporanRincianBelanjaAllOpdResponse(
                    200,
                    "success get laporan rincian belanja",
                    List.of()
            );
        }

        Map<String, List<RencanaKinerja>> rencanaByOpd = rencanaKinerjaList.stream()
                .filter(rencanaKinerja -> rencanaKinerja.kodeOpd() != null && !rencanaKinerja.kodeOpd().isBlank())
                .collect(Collectors.groupingBy(RencanaKinerja::kodeOpd, TreeMap::new, Collectors.toList()));

        List<LaporanRincianBelanjaOpdResponse> data = new ArrayList<>();

        for (Map.Entry<String, List<RencanaKinerja>> entry : rencanaByOpd.entrySet()) {
            String kodeOpd = entry.getKey();
            List<RencanaKinerja> rkList = entry.getValue();
            LaporanRincianBelanjaEnvelopeResponse envelope = buildLaporanRincianBelanja(rkList, kodeOpd, tahun);
            if (envelope.data().isEmpty()) {
                continue;
            }

            String namaOpd = opdRepository.findByKodeOpd(kodeOpd)
                    .map(Opd::namaOpd)
                    .orElse(null);

            data.add(new LaporanRincianBelanjaOpdResponse(
                    kodeOpd,
                    namaOpd,
                    envelope.data()
            ));
        }

        return new LaporanRincianBelanjaAllOpdResponse(
                200,
                "success get laporan rincian belanja",
                data
        );
    }

    private LaporanRincianBelanjaEnvelopeResponse buildLaporanRincianBelanja(
            List<RencanaKinerja> rencanaKinerjaList,
            String kodeOpd,
            Integer tahun
    ) {
        List<RincianBelanja> rincianBelanjaRecords =
                rincianBelanjaRepository.findByKodeOpdAndTahun(kodeOpd, tahun);

        Map<Long, RincianBelanja> rincianBelanjaByRencanaAksiId = rincianBelanjaRecords.stream()
                .filter(r -> r.idRencanaAksi() != null)
                .collect(Collectors.toMap(
                        RincianBelanja::idRencanaAksi,
                        Function.identity(),
                        (a, b) -> a,
                        HashMap::new
                ));

        Map<String, SubkegiatanAccumulator> subkegiatanMap = new TreeMap<>();

        for (RencanaKinerja rencanaKinerja : rencanaKinerjaList) {
            List<LaporanRincianBelanjaIndikatorResponse> indikatorResponses =
                    buildIndikatorResponses(rencanaKinerja.id());

            List<SubKegiatanRencanaKinerja> subkegiatanList =
                    subKegiatanRencanaKinerjaRepository.findByIdRekin(rencanaKinerja.id().intValue());

            List<RencanaAksi> rencanaAksiList = StreamSupport
                    .stream(rencanaAksiRepository.findByIdRekinOrderByUrutan(rencanaKinerja.id().intValue()).spliterator(), false)
                    .toList();

            for (SubKegiatanRencanaKinerja subkegiatan : subkegiatanList) {
                List<LaporanRincianBelanjaRencanaAksiResponse> rencanaAksiResponses = new ArrayList<>();
                int totalAnggaranRencanaKinerja = 0;

                for (RencanaAksi rencanaAksi : rencanaAksiList) {
                    RincianBelanja rincianBelanja = rincianBelanjaByRencanaAksiId.get(rencanaAksi.id());
                    if (rincianBelanja == null) {
                        continue;
                    }
                    if (!Objects.equals(rincianBelanja.idSubkegiatanRencanaKinerja(), subkegiatan.id())) {
                        continue;
                    }

                    int anggaran = rincianBelanja.anggaran() == null ? 0 : rincianBelanja.anggaran();
                    totalAnggaranRencanaKinerja += anggaran;

                    rencanaAksiResponses.add(new LaporanRincianBelanjaRencanaAksiResponse(
                            rencanaAksi.id(),
                            rencanaAksi.namaRencanaAksi(),
                            anggaran
                    ));
                }

                if (rencanaAksiResponses.isEmpty()) {
                    continue;
                }

                LaporanRincianBelanjaRencanaKinerjaResponse rincianBelanjaResponse =
                        new LaporanRincianBelanjaRencanaKinerjaResponse(
                                rencanaKinerja.id(),
                                rencanaKinerja.rencanaKinerja(),
                                rencanaKinerja.nipPegawai(),
                                rencanaKinerja.namaPegawai(),
                                indikatorResponses,
                                totalAnggaranRencanaKinerja,
                                rencanaAksiResponses
                        );

                String key = subkegiatan.kodeSubKegiatan() == null
                        ? "subkegiatan-" + subkegiatan.id()
                        : subkegiatan.kodeSubKegiatan();

                SubkegiatanAccumulator accumulator = subkegiatanMap.computeIfAbsent(
                        key,
                        k -> new SubkegiatanAccumulator(subkegiatan.kodeSubKegiatan(), subkegiatan.namaSubKegiatan())
                );

                accumulator.addRincianBelanja(rincianBelanjaResponse, totalAnggaranRencanaKinerja, indikatorResponses);
            }
        }

        List<LaporanRincianBelanjaSubkegiatanResponse> data = subkegiatanMap.values().stream()
                .map(SubkegiatanAccumulator::toResponse)
                .toList();

        return new LaporanRincianBelanjaEnvelopeResponse(
                200,
                "success get laporan rincian belanja",
                data
        );
    }

    public LaporanRincianBelanjaEnvelopeResponse getLaporanRincianBelanjaVerified(
            Long idRencanaKinerja,
            String requesterNip,
            boolean isLevel2,
            String filterHash
    ) {
        // filterHash tetap diterima untuk kompatibilitas, tetapi tidak digunakan.
        RencanaKinerja rencanaKinerja = rencanaKinerjaRepository.findById(idRencanaKinerja).orElse(null);
        if (rencanaKinerja == null) {
            return new LaporanRincianBelanjaEnvelopeResponse(
                    200,
                    "success get laporan rincian belanja",
                    List.of()
            );
        }

        List<RencanaKinerjaVerifikator> verifikatorList =
                rencanaKinerjaVerifikatorRepository.findByIdRencanaKinerja(rencanaKinerja.id());

        if (verifikatorList.isEmpty()) {
            return new LaporanRincianBelanjaEnvelopeResponse(
                    200,
                    "success get laporan rincian belanja",
                    List.of()
            );
        }

        if (isLevel2) {
            boolean allowed = verifikatorList.stream()
                    .anyMatch(relasi -> Objects.equals(relasi.nipVerifikator(), requesterNip));
            if (!allowed) {
                return new LaporanRincianBelanjaEnvelopeResponse(
                        200,
                        "success get laporan rincian belanja",
                        List.of()
                );
            }
        }

        return buildLaporanRincianBelanja(
                List.of(rencanaKinerja),
                rencanaKinerja.kodeOpd(),
                rencanaKinerja.tahun()
        );
    }

    private List<LaporanRincianBelanjaIndikatorResponse> buildIndikatorResponses(Long rencanaKinerjaId) {
        List<Indikator> indikatorList = indikatorRepository.findByRencanaKinerjaId(rencanaKinerjaId);

        return indikatorList.stream()
                .map(indikator -> {
                    List<Target> targetList = targetRepository.findByIndikatorId(indikator.id());
                    List<LaporanRincianBelanjaTargetResponse> targets = targetList.stream()
                            .map(t -> new LaporanRincianBelanjaTargetResponse(t.id(), t.target(), t.satuan()))
                            .toList();

                    return new LaporanRincianBelanjaIndikatorResponse(
                            indikator.id(),
                            indikator.namaIndikator(),
                            targets
                    );
                })
                .toList();
    }

    private static final class SubkegiatanAccumulator {
        private final String kodeSubkegiatan;
        private final String namaSubkegiatan;
        private final List<LaporanRincianBelanjaRencanaKinerjaResponse> rincianBelanja = new ArrayList<>();
        private final Set<String> indikatorNames = new HashSet<>();
        private int totalAnggaran = 0;

        private SubkegiatanAccumulator(String kodeSubkegiatan, String namaSubkegiatan) {
            this.kodeSubkegiatan = kodeSubkegiatan;
            this.namaSubkegiatan = namaSubkegiatan;
        }

        private void addRincianBelanja(
                LaporanRincianBelanjaRencanaKinerjaResponse response,
                int totalAnggaranRencanaKinerja,
                List<LaporanRincianBelanjaIndikatorResponse> indikatorResponses
        ) {
            rincianBelanja.add(response);
            totalAnggaran += totalAnggaranRencanaKinerja;
            indikatorNames.addAll(indikatorResponses.stream()
                    .map(LaporanRincianBelanjaIndikatorResponse::namaIndikator)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        }

        private LaporanRincianBelanjaSubkegiatanResponse toResponse() {
            String indikatorSubkegiatan = indikatorNames.size() == 1
                    ? indikatorNames.iterator().next()
                    : null;

            return new LaporanRincianBelanjaSubkegiatanResponse(
                    kodeSubkegiatan,
                    namaSubkegiatan,
                    indikatorSubkegiatan,
                    totalAnggaran,
                    rincianBelanja.stream()
                            .sorted((a, b) -> a.rencanaKinerjaId().compareTo(b.rencanaKinerjaId()))
                            .toList()
            );
        }
    }

}
