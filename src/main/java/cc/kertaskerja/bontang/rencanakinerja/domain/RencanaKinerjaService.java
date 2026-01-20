package cc.kertaskerja.bontang.rencanakinerja.domain;

import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaNotFoundException;
import cc.kertaskerja.bontang.rencanakinerja.web.RencanaKinerjaRequest;
import cc.kertaskerja.bontang.target.domain.TargetService;
import cc.kertaskerja.bontang.target.domain.Target;
import cc.kertaskerja.bontang.indikator.domain.IndikatorService;
import cc.kertaskerja.bontang.indikator.domain.Indikator;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksiService;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi;
import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain.PelaksanaanService;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerjaService;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;
import cc.kertaskerja.bontang.dasarhukum.domain.DasarHukumService;
import cc.kertaskerja.bontang.dasarhukum.domain.DasarHukum;
import cc.kertaskerja.bontang.gambaranumum.domain.GambaranUmumService;
import cc.kertaskerja.bontang.gambaranumum.domain.GambaranUmum;

import cc.kertaskerja.bontang.rencanakinerja.web.response.RencanaKinerjaDetailResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.IndikatorDetailResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.RencanaAksiResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.SubkegiatanResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.DasarHukumResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.GambaranUmumResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.TotalPerBulanResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.IndikatorCreateResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.RencanaKinerjaCreateResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.SimpleRencanaKinerjaResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.SimpleIndikatorResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.SimpleTargetResponse;
import cc.kertaskerja.bontang.rencanakinerja.web.response.SumberDanaResponse;

import cc.kertaskerja.bontang.sumberdana.domain.SumberDana;
import cc.kertaskerja.bontang.sumberdana.domain.SumberDanaRepository;

import java.util.stream.StreamSupport;

@Service
public class RencanaKinerjaService {
    private static final int TOTAL_BOBOT_AWAL = 100;

    private RencanaKinerjaRepository rencanaKinerjaRepository;
    private RencanaKinerjaSumberDanaRepository rencanaKinerjaSumberDanaRepository;
    private SumberDanaRepository sumberDanaRepository;
    private TargetService targetService;
    private IndikatorService indikatorService;
    private RencanaAksiService rencanaAksiService;
    private PelaksanaanService pelaksanaanService;
    private SubKegiatanRencanaKinerjaService subKegiatanRencanaKinerjaService;
    private DasarHukumService dasarHukumService;
    private GambaranUmumService gambaranUmumService;

    public RencanaKinerjaService(RencanaKinerjaRepository rencanaKinerjaRepository,
                           RencanaKinerjaSumberDanaRepository rencanaKinerjaSumberDanaRepository,
                           SumberDanaRepository sumberDanaRepository,
                           TargetService targetService,
                           IndikatorService indikatorService,
                           RencanaAksiService rencanaAksiService,
                           PelaksanaanService pelaksanaanService,
                           SubKegiatanRencanaKinerjaService subKegiatanRencanaKinerjaService,
                           DasarHukumService dasarHukumService,
                           GambaranUmumService gambaranUmumService) {
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.rencanaKinerjaSumberDanaRepository = rencanaKinerjaSumberDanaRepository;
        this.sumberDanaRepository = sumberDanaRepository;
        this.targetService = targetService;
        this.indikatorService = indikatorService;
        this.rencanaAksiService = rencanaAksiService;
        this.pelaksanaanService = pelaksanaanService;
        this.subKegiatanRencanaKinerjaService = subKegiatanRencanaKinerjaService;
        this.dasarHukumService = dasarHukumService;
        this.gambaranUmumService = gambaranUmumService;
    }

    private List<SumberDanaResponse> buildSumberDanaResponses(Long rencanaKinerjaId) {
        List<RencanaKinerjaSumberDana> relasiList = rencanaKinerjaSumberDanaRepository.findByIdRencanaKinerja(rencanaKinerjaId);

        return relasiList.stream()
            .map(relasi -> {
                SumberDana sumberDana = sumberDanaRepository.findById(relasi.idSumberDana())
                    .orElse(null);
                return sumberDana != null ? SumberDanaResponse.from(sumberDana) : null;
            })
            .filter(Objects::nonNull)
            .toList();
    }

    private void syncSumberDana(Long rencanaKinerjaId, List<Long> sumberDanaIds) {
        rencanaKinerjaSumberDanaRepository.deleteByIdRencanaKinerja(rencanaKinerjaId);

        if (sumberDanaIds != null && !sumberDanaIds.isEmpty()) {
            List<RencanaKinerjaSumberDana> relasiList = sumberDanaIds.stream()
                .map(sumberDanaId -> RencanaKinerjaSumberDana.of(rencanaKinerjaId, sumberDanaId))
                .toList();

            rencanaKinerjaSumberDanaRepository.saveAll(relasiList);
        }
    }

    private void validateSumberDanaIds(List<Long> sumberDanaIds) {
        if (sumberDanaIds == null || sumberDanaIds.isEmpty()) {
            return;
        }

        for (Long sumberDanaId : sumberDanaIds) {
            if (!sumberDanaRepository.existsById(sumberDanaId)) {
                throw new IllegalArgumentException("Sumber dana dengan id " + sumberDanaId + " tidak ditemukan");
            }
        }
    }

    public Iterable<RencanaKinerja> findAll() {
        return rencanaKinerjaRepository.findAll();
    }

    public Map<String, Object> findByNipPegawaiAndKodeOpdAndTahun(String nipPegawai, String kodeOpd, Integer tahun) {
        List<RencanaKinerja> rencanaKinerjas = rencanaKinerjaRepository.findByNipPegawaiAndKodeOpdAndTahun(nipPegawai, kodeOpd, tahun);

        if (rencanaKinerjas.isEmpty()) {
            throw new RencanaKinerjaNotFoundException(nipPegawai, kodeOpd, tahun);
        }

            return buildSimpleRencanaKinerjaResponse(rencanaKinerjas);
        }

    public Map<String, Object> findDetailByIdAndNipPegawai(Long idRencanaKinerja, String nipPegawai) {
        RencanaKinerja rencanaKinerja = rencanaKinerjaRepository.findByIdAndNipPegawai(idRencanaKinerja, nipPegawai)
                .orElseThrow(() -> new RencanaKinerjaNotFoundException("Rencana Kinerja dengan id " + idRencanaKinerja + " dan nip " + nipPegawai + " tidak ditemukan"));

        return buildRencanaKinerjaDetailResponse(rencanaKinerja);
    }

    // Untuk endpoint getByNipPegawaiAndKodeOpdAndTahun yang hanya menampilkan data yang diperlukan
    private Map<String, Object> buildSimpleRencanaKinerjaResponse(List<RencanaKinerja> rencanaKinerjas) {
        List<SimpleRencanaKinerjaResponse> rencanaKinerjaList = rencanaKinerjas.stream()
            .map(rencanaKinerja -> {
                List<Indikator> indikators = indikatorService.findByRencanaKinerjaId(rencanaKinerja.id());

                List<SimpleIndikatorResponse> indikatorResponses = indikators.stream()
                    .map(indikator -> {
                        List<Target> targets = targetService.findByIndikatorId(indikator.id());
                        return SimpleIndikatorResponse.from(indikator, targets);
                    })
                    .toList();

                List<SumberDanaResponse> sumberDanaList = buildSumberDanaResponses(rencanaKinerja.id());

                return SimpleRencanaKinerjaResponse.from(rencanaKinerja, sumberDanaList, indikatorResponses);
            })
            .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("rencana_kinerja", rencanaKinerjaList.stream()
            .map(SimpleRencanaKinerjaResponse::toMap)
            .toList());

        return response;
    }

    // Untuk method findByNipPegawaiAndKodeOpdAndTahun
    private Map<String, Object> buildRencanaKinerjaDetailResponse(RencanaKinerja rencanaKinerja) {
        List<IndikatorDetailResponse> indikatorList = buildIndikatorResponses(rencanaKinerja.id());

        List<SubkegiatanResponse> subkegiatanList = buildSubkegiatanResponses(rencanaKinerja.id());

        List<DasarHukumResponse> dasarHukumList = buildDasarHukumResponses(rencanaKinerja.id());

        List<GambaranUmumResponse> gambaranUmumList = buildGambaranUmumResponses(rencanaKinerja.id());

        List<SumberDanaResponse> sumberDanaList = buildSumberDanaResponses(rencanaKinerja.id());

        RencanaAksiCalculationResult calculationResult = buildRencanaAksiResponses(rencanaKinerja);
        List<RencanaAksiResponse> rencanaAksiList = calculationResult.rencanaAksiResponses();

        List<TotalPerBulanResponse> totalPerBulanList = buildTotalPerBulanResponses(
            calculationResult.totalBobotPerBulan()
        );

        int totalKeseluruhan = calculateTotalKeseluruhan(calculationResult.totalBobotPerBulan());
        int waktuDibutuhkan = calculateWaktuDibutuhkan(calculationResult.bulanMemilikiData());

        // Build main response
        RencanaKinerjaDetailResponse detailResponse = RencanaKinerjaDetailResponse.from(
            rencanaKinerja,
            sumberDanaList,
            indikatorList,
            subkegiatanList,
            dasarHukumList,
            gambaranUmumList,
            rencanaAksiList,
            totalPerBulanList,
            totalKeseluruhan,
            waktuDibutuhkan
        );

        // Convert to Map for controller compatibility
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("rencanaKinerja", detailResponse.toMap());

        return response;
    }

    // Helper methods for building individual response components
    private List<IndikatorDetailResponse> buildIndikatorResponses(Long rencanaKinerjaId) {
        List<Indikator> indikators = indikatorService.findByRencanaKinerjaId(rencanaKinerjaId);

        return indikators.stream()
            .map(indikator -> {
                List<Target> targets = targetService.findByIndikatorId(indikator.id());
                return IndikatorDetailResponse.from(indikator, targets);
            })
            .toList();
    }

    private List<SubkegiatanResponse> buildSubkegiatanResponses(Long rencanaKinerjaId) {
        List<SubKegiatanRencanaKinerja> subkegiatans =
            subKegiatanRencanaKinerjaService.findByIdRekin(rencanaKinerjaId.intValue());

        return subkegiatans.stream()
            .map(SubkegiatanResponse::from)
            .toList();
    }

    private List<DasarHukumResponse> buildDasarHukumResponses(Long rencanaKinerjaId) {
        Iterable<DasarHukum> dasarHukums = dasarHukumService.findByIdRencanaKinerja(rencanaKinerjaId);

        return StreamSupport.stream(dasarHukums.spliterator(), false)
            .map(dasarHukum -> DasarHukumResponse.from(dasarHukum, rencanaKinerjaId))
            .toList();
    }

    private List<GambaranUmumResponse> buildGambaranUmumResponses(Long rencanaKinerjaId) {
        Iterable<GambaranUmum> gambaranUmums = gambaranUmumService.findByIdRencanaKinerja(rencanaKinerjaId);

        return StreamSupport.stream(gambaranUmums.spliterator(), false)
            .map(gambaranUmum -> GambaranUmumResponse.from(gambaranUmum, rencanaKinerjaId))
            .toList();
    }

    private RencanaAksiCalculationResult buildRencanaAksiResponses(RencanaKinerja rencanaKinerja) {
        Iterable<RencanaAksi> rencanaAksis = rencanaAksiService.findByIdRekinOrderByUrutan(
            rencanaKinerja.id().intValue()
        );

        int[] totalBobotPerBulan = new int[12];
        boolean[] bulanMemilikiData = new boolean[12];
        List<RencanaAksiResponse> rencanaAksiResponses = new ArrayList<>();

        // Inisialisasi sisa bobot dengan nilai awal 100
        int sisaBobot = TOTAL_BOBOT_AWAL;

        for (RencanaAksi rencanaAksi : rencanaAksis) {
            List<Map<String, Object>> pelaksanaanList =
                pelaksanaanService.buildPelaksanaanResponseList(rencanaAksi.id().intValue());

            // Hitung jumlah bobot dan update sisa bobot
            BobotCalculationResult calculationResult = calculateJumlahBobot(
                pelaksanaanList,
                totalBobotPerBulan,
                bulanMemilikiData,
                sisaBobot
            );

            int jumlahBobot = calculationResult.jumlahBobot();
            sisaBobot = calculationResult.sisaBobotBaru();

            RencanaAksiResponse response = RencanaAksiResponse.from(
                rencanaAksi,
                rencanaKinerja.id(),
                pelaksanaanList,
                jumlahBobot,
                sisaBobot
            );
            rencanaAksiResponses.add(response);
        }

        return new RencanaAksiCalculationResult(rencanaAksiResponses, totalBobotPerBulan, bulanMemilikiData);
    }

    private BobotCalculationResult calculateJumlahBobot(
        List<Map<String, Object>> pelaksanaanList,
        int[] totalBobotPerBulan,
        boolean[] bulanMemilikiData,
        int sisaBobot
    ) {
        int jumlahBobot = 0;

        for (Map<String, Object> pelaksanaan : pelaksanaanList) {
            Integer bobot = (Integer) pelaksanaan.get("bobot");
            Integer bulan = (Integer) pelaksanaan.get("bulan");
            Object idObj = pelaksanaan.get("id");
            jumlahBobot += bobot;

            if (bulan != null && bulan >= 1 && bulan <= 12) {
                totalBobotPerBulan[bulan - 1] += bobot;
                if (idObj != null && !idObj.equals(0)) {
                    bulanMemilikiData[bulan - 1] = true;
                }
            }

            // Kurangi sisaBobot
            if (bobot != null && bobot > 0) {
                sisaBobot -= bobot;
            }
        }

        return new BobotCalculationResult(jumlahBobot, sisaBobot);
    }

    private List<TotalPerBulanResponse> buildTotalPerBulanResponses(int[] totalBobotPerBulan) {
        List<TotalPerBulanResponse> totalPerBulanList = new ArrayList<>();

        for (int bulan = 1; bulan <= 12; bulan++) {
            totalPerBulanList.add(TotalPerBulanResponse.create(bulan, totalBobotPerBulan[bulan - 1]));
        }

        return totalPerBulanList;
    }

    private int calculateTotalKeseluruhan(int[] totalBobotPerBulan) {
        int total = 0;
        for (int bobot : totalBobotPerBulan) {
            total += bobot;
        }
        return total;
    }

    private int calculateWaktuDibutuhkan(boolean[] bulanMemilikiData) {
        int waktu = 0;
        for (boolean hasData : bulanMemilikiData) {
            if (hasData) {
                waktu++;
            }
        }
        return waktu;
    }

    // Helper record untuk hasil kalkulasi
    private record RencanaAksiCalculationResult(
        List<RencanaAksiResponse> rencanaAksiResponses,
        int[] totalBobotPerBulan,
        boolean[] bulanMemilikiData
    ) {}

    // Helper record untuk hasil kalkulasi bobot
    private record BobotCalculationResult(int jumlahBobot, int sisaBobotBaru) {}

    @Transactional
    public ResponseEntity<Map<String, Object>> tambahRencanaKinerja(RencanaKinerjaRequest request) {
        validateSumberDanaIds(request.sumberDanaIds());

        RencanaKinerja rencanaKinerja = RencanaKinerja.of(
                null,
                request.rencanaKinerja(),
                request.kodeOpd(),
                request.nipPegawai(),
                request.createdBy(),
                request.tahun(),
                request.statusRencanaKinerja(),
                request.namaOpd(),
                request.namaPegawai(),
                request.keterangan()
        );
        RencanaKinerja savedRencanaKinerja = rencanaKinerjaRepository.save(rencanaKinerja);

        syncSumberDana(savedRencanaKinerja.id(), request.sumberDanaIds());

        return buildResponse(savedRencanaKinerja, request.indikatorList(), request.sumberDanaIds());
    }

    // Untuk method tambahRencanaKinerja
    private ResponseEntity<Map<String, Object>> buildResponse(
            RencanaKinerja savedRencanaKinerja,
            List<RencanaKinerjaRequest.IndikatorData> indikatorList,
            List<Long> sumberDanaIds) {

        List<SumberDanaResponse> sumberDanaList = List.of();
        if (sumberDanaIds != null && !sumberDanaIds.isEmpty()) {
            sumberDanaList = sumberDanaIds.stream()
                .map(sumberDanaId -> {
                    SumberDana sumberDana = sumberDanaRepository.findById(sumberDanaId).orElse(null);
                    return sumberDana != null ? SumberDanaResponse.from(sumberDana) : null;
                })
                .filter(Objects::nonNull)
                .toList();
        }

        List<IndikatorCreateResponse> indikatorResponses = buildIndikatorWithTargetsForCreate(
            indikatorList,
            savedRencanaKinerja.id()
        );

        RencanaKinerjaCreateResponse createResponse = RencanaKinerjaCreateResponse.from(
            savedRencanaKinerja,
            sumberDanaList,
            indikatorResponses
        );

        return ResponseEntity.ok(createResponse.toMap());
    }

    // Helper method untuk build indikator dengan targets saat create
    private List<IndikatorCreateResponse> buildIndikatorWithTargetsForCreate(
            List<RencanaKinerjaRequest.IndikatorData> indikatorList,
            Long rencanaKinerjaId) {

        if (indikatorList == null || indikatorList.isEmpty()) {
            return List.of();
        }

        return indikatorList.stream()
            .map(indikatorData -> {
                Indikator savedIndikator = indikatorService.tambahIndikator(
                    indikatorData.namaIndikator(),
                    rencanaKinerjaId
                );

                List<Target> targets = List.of();
                List<RencanaKinerjaRequest.TargetData> targetList = indikatorData.targetList();
                if (targetList != null && !targetList.isEmpty()) {
                    targets = targetList.stream()
                        .map(targetData -> targetService.tambahTarget(
                            targetData.target(),
                            targetData.satuan(),
                            savedIndikator.id()
                        ))
                        .toList();
                }

                return IndikatorCreateResponse.from(savedIndikator, targets);
            })
            .toList();
    }

    // Method untuk memproses update target
    private List<Target> buildTargetsForUpdate(
            Long indikatorId,
            List<RencanaKinerjaRequest.TargetData> targetList) {

        List<Target> targetsList = new ArrayList<>();

        List<Target> existingTargets = targetService.findByIndikatorId(indikatorId);

        // Hapus target yang tidak ada di request
        Set<Long> requestTargetIds = new HashSet<>();
        if (targetList != null) {
            requestTargetIds = targetList.stream()
                .map(RencanaKinerjaRequest.TargetData::idTarget)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }

        for (Target existing : existingTargets) {
            if (!requestTargetIds.contains(existing.id())) {
                targetService.hapusTarget(existing.id());
            }
        }

        // Proses target dari request
        if (targetList != null && !targetList.isEmpty()) {
            for (RencanaKinerjaRequest.TargetData targetData : targetList) {
                Target target;

                if (targetData.idTarget() != null) {
                    Target existing = targetService.detailTargetById(targetData.idTarget());
                    target = new Target(
                        existing.id(),
                        targetData.target() != null ? targetData.target() : existing.target(),
                        targetData.satuan() != null ? targetData.satuan() : existing.satuan(),
                        existing.indikatorId(),
                        existing.createdDate(),
                        null
                    );
                    target = targetService.ubahTarget(existing.id(), new cc.kertaskerja.bontang.target.web.TargetRequest(
                        existing.id(),
                        target.target(),
                        target.satuan(),
                        target.indikatorId()
                    ));
                } else {
                    target = targetService.tambahTarget(
                        targetData.target(),
                        targetData.satuan(),
                        indikatorId
                    );
                }

                targetsList.add(target);
            }
        }

        return targetsList;
    }

    // Method untuk memproses update indikator
    private List<cc.kertaskerja.bontang.rencanakinerja.web.response.IndikatorUpdateResponse> buildIndikatorWithTargetsForUpdate(
            RencanaKinerja savedRencanaKinerja,
            List<RencanaKinerjaRequest.IndikatorData> indikatorList) {

        List<cc.kertaskerja.bontang.rencanakinerja.web.response.IndikatorUpdateResponse> indikatorResponseList = new ArrayList<>();

        List<Indikator> existingIndikators = indikatorService.findByRencanaKinerjaId(savedRencanaKinerja.id());

        Set<Long> requestIndikatorIds = new HashSet<>();
        if (indikatorList != null) {
            requestIndikatorIds = indikatorList.stream()
                .map(RencanaKinerjaRequest.IndikatorData::idIndikator)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }

        for (Indikator existing : existingIndikators) {
            if (!requestIndikatorIds.contains(existing.id())) {
                // Hapus indikator dan target terkait
                indikatorService.hapusIndikator(existing.id());
            }
        }

        // Proses indikator dari request
        if (indikatorList != null && !indikatorList.isEmpty()) {
            for (RencanaKinerjaRequest.IndikatorData indikatorData : indikatorList) {
                Indikator indikator;

                if (indikatorData.idIndikator() != null) {
                    Indikator existing = indikatorService.detailIndikatorById(indikatorData.idIndikator());
                    indikator = new Indikator(
                        existing.id(),
                        indikatorData.namaIndikator() != null ? indikatorData.namaIndikator() : existing.namaIndikator(),
                        existing.rencanaKinerjaId(),
                        existing.createdDate(),
                        null
                    );
                    indikator = indikatorService.ubahIndikator(existing.id(), indikator);
                } else {
                    indikator = indikatorService.tambahIndikator(
                        indikatorData.namaIndikator(),
                        savedRencanaKinerja.id()
                    );
                }

                // Proses target
                List<Target> targets = buildTargetsForUpdate(
                    indikator.id(),
                    indikatorData.targetList()
                );

                indikatorResponseList.add(cc.kertaskerja.bontang.rencanakinerja.web.response.IndikatorUpdateResponse.from(
                    indikator,
                    targets
                ));
            }
        }

        return indikatorResponseList;
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> ubahRencanaKinerja(Long id, RencanaKinerjaRequest request) {
        validateSumberDanaIds(request.sumberDanaIds());

        RencanaKinerja existingRencanaKinerja = rencanaKinerjaRepository.findById(id)
                .orElseThrow(() -> new RencanaKinerjaNotFoundException(id));

        RencanaKinerja updatedRencanaKinerja = new RencanaKinerja(
                id,
                null,
                request.rencanaKinerja(),
                request.kodeOpd() != null ? request.kodeOpd() : existingRencanaKinerja.kodeOpd(),
                request.nipPegawai() != null ? request.nipPegawai() : existingRencanaKinerja.nipPegawai(),
                request.createdBy() != null ? request.createdBy() : existingRencanaKinerja.createdBy(),
                request.tahun() != null ? request.tahun() : existingRencanaKinerja.tahun(),
                request.statusRencanaKinerja() != null ? request.statusRencanaKinerja() : existingRencanaKinerja.statusRencanaKinerja(),
                request.namaOpd() != null ? request.namaOpd() : existingRencanaKinerja.namaOpd(),
                request.namaPegawai() != null ? request.namaPegawai() : existingRencanaKinerja.namaPegawai(),
                request.keterangan() != null ? request.keterangan() : existingRencanaKinerja.keterangan(),
                existingRencanaKinerja.createdDate(),
                existingRencanaKinerja.lastModifiedDate()
        );

        RencanaKinerja savedRencanaKinerja = rencanaKinerjaRepository.save(updatedRencanaKinerja);

        syncSumberDana(savedRencanaKinerja.id(), request.sumberDanaIds());

        List<cc.kertaskerja.bontang.rencanakinerja.web.response.IndikatorUpdateResponse> indikatorResponses =
            buildIndikatorWithTargetsForUpdate(savedRencanaKinerja, request.indikatorList());

        List<SumberDanaResponse> sumberDanaList = buildSumberDanaResponses(savedRencanaKinerja.id());

        cc.kertaskerja.bontang.rencanakinerja.web.response.RencanaKinerjaUpdateResponse response =
            cc.kertaskerja.bontang.rencanakinerja.web.response.RencanaKinerjaUpdateResponse.from(
                savedRencanaKinerja,
                sumberDanaList,
                indikatorResponses
            );

        return ResponseEntity.ok(response.toMap());
    }

    public void hapusRencanaKinerja(Long id) {

        rencanaKinerjaRepository.deleteById(id);
    }
}
