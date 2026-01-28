package cc.kertaskerja.bontang.laporanrincianbelanja.domain;

import cc.kertaskerja.bontang.indikator.domain.Indikator;
import cc.kertaskerja.bontang.indikator.domain.IndikatorRepository;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaEnvelopeResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaIndikatorResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaRencanaAksiResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaRencanaKinerjaResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaSubkegiatanResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaTargetResponse;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksiRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
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

    public LaporanRincianBelanjaService(
            RencanaKinerjaRepository rencanaKinerjaRepository,
            SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository,
            IndikatorRepository indikatorRepository,
            TargetRepository targetRepository,
            RencanaAksiRepository rencanaAksiRepository,
            RincianBelanjaRepository rincianBelanjaRepository
    ) {
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.subKegiatanRencanaKinerjaRepository = subKegiatanRencanaKinerjaRepository;
        this.indikatorRepository = indikatorRepository;
        this.targetRepository = targetRepository;
        this.rencanaAksiRepository = rencanaAksiRepository;
        this.rincianBelanjaRepository = rincianBelanjaRepository;
    }

    public LaporanRincianBelanjaEnvelopeResponse getLaporanRincianBelanja(
            String kodeOpd,
            Integer tahun
    ) {
        List<RencanaKinerja> rencanaKinerjaList =
                rencanaKinerjaRepository.findByKodeOpdAndTahun(kodeOpd, tahun);

        if (rencanaKinerjaList.isEmpty()) {
            return new LaporanRincianBelanjaEnvelopeResponse(
                    200,
                    "success get laporan rincian belanja",
                    List.of()
            );
        }

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
