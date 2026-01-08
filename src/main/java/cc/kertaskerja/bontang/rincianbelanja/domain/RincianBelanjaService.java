package cc.kertaskerja.bontang.rincianbelanja.domain;

import cc.kertaskerja.bontang.indikatorbelanja.domain.IndikatorBelanja;
import cc.kertaskerja.bontang.indikatorbelanja.web.IndikatorBelanjaRequest;
import cc.kertaskerja.bontang.rinciananggaran.domain.RincianAnggaran;
import cc.kertaskerja.bontang.rinciananggaran.domain.RincianAnggaranService;
import cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain.KodeRekeningRincianAnggaranService;
import cc.kertaskerja.bontang.rinciananggaran.pelaksanaanrinciananggaran.domain.PelaksanaanRincianAnggaranService;
import cc.kertaskerja.bontang.rincianbelanja.web.response.IndikatorDetailResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.KodeRekeningResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianAnggaranResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.TargetResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.SubkegiatanResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.TotalPerBulanResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.SimpleIndikatorResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.IndikatorCreateResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaCreateResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaDetailResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.SimpleRincianBelanjaResponse;
import cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain.SubKegiatanRincianBelanja;
import cc.kertaskerja.bontang.subkegiatanrincianbelanja.domain.SubKegiatanRincianBelanjaService;
import cc.kertaskerja.bontang.indikatorbelanja.domain.IndikatorBelanjaService;
import cc.kertaskerja.bontang.rincianbelanja.domain.exception.RincianBelanjaNotFoundException;
import cc.kertaskerja.bontang.rincianbelanja.web.RincianBelanjaRequest;
import cc.kertaskerja.bontang.targetbelanja.domain.TargetBelanja;
import cc.kertaskerja.bontang.targetbelanja.domain.TargetBelanjaService;
import cc.kertaskerja.bontang.targetbelanja.web.TargetBelanjaRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RincianBelanjaService {
    private final RincianBelanjaRepository rincianBelanjaRepository;
    private final IndikatorBelanjaService indikatorBelanjaService;
    private final TargetBelanjaService targetBelanjaService;
    private final SubKegiatanRincianBelanjaService subKegiatanRincianBelanjaService;
    private final RincianAnggaranService rincianAnggaranService;
    private final PelaksanaanRincianAnggaranService pelaksanaanRincianAnggaranService;
    private final KodeRekeningRincianAnggaranService kodeRekeningRincianAnggaranService;

    public RincianBelanjaService(RincianBelanjaRepository rincianBelanjaRepository, IndikatorBelanjaService indikatorBelanjaService, TargetBelanjaService targetBelanjaService, SubKegiatanRincianBelanjaService subKegiatanRincianBelanjaService, RincianAnggaranService rincianAnggaranService, PelaksanaanRincianAnggaranService pelaksanaanRincianAnggaranService, KodeRekeningRincianAnggaranService kodeRekeningRincianAnggaranService) {
        this.rincianBelanjaRepository = rincianBelanjaRepository;
        this.indikatorBelanjaService = indikatorBelanjaService;
        this.targetBelanjaService = targetBelanjaService;
        this.subKegiatanRincianBelanjaService = subKegiatanRincianBelanjaService;
        this.rincianAnggaranService = rincianAnggaranService;
        this.pelaksanaanRincianAnggaranService = pelaksanaanRincianAnggaranService;
        this.kodeRekeningRincianAnggaranService = kodeRekeningRincianAnggaranService;
    }

    public Map<String, Object> findByNipPegawaiAndKodeOpdAndTahun(String nipPegawai, String kodeOpd, Integer tahun) {
        List<RincianBelanja> rincianBelanjas = rincianBelanjaRepository.findByNipPegawaiAndKodeOpdAndTahun(nipPegawai, kodeOpd, tahun);

        if (rincianBelanjas.isEmpty()) {
            throw new RincianBelanjaNotFoundException(nipPegawai, kodeOpd, tahun);
        }

        return buildSimpleRincianBelanjaResponse(rincianBelanjas);
    }

    private Map<String, Object> buildSimpleRincianBelanjaResponse(List<RincianBelanja> rincianBelanjas) {
        List<SimpleRincianBelanjaResponse> rincianBelanjaList = rincianBelanjas.stream()
            .<SimpleRincianBelanjaResponse>map(rincianBelanja -> {
                List<IndikatorBelanja> indikatorBelanjas = indikatorBelanjaService.findByRincianBelanjaId(rincianBelanja.id());

                List<SimpleIndikatorResponse> indikatorResponses = indikatorBelanjas.stream()
                    .map(indikatorBelanja -> {
                        List<TargetBelanja> targetBelanjas = targetBelanjaService.findByIndikatorBelanjaId(indikatorBelanja.id());
                        return SimpleIndikatorResponse.from(indikatorBelanja, targetBelanjas);
                    })
                    .toList();

                return SimpleRincianBelanjaResponse.from(rincianBelanja, indikatorResponses);
            })
            .toList();

        List<Map<String, Object>> rincianBelanjaResponseList = rincianBelanjaList.stream()
            .map(SimpleRincianBelanjaResponse::toMap)
            .toList();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("rincian_belanja", rincianBelanjaResponseList);

        return response;
    }

    public Map<String, Object> findDetailByIdAndNipPegawai(Long idRincianBelanja, String nipPegawai) {
        RincianBelanja rincianBelanja = rincianBelanjaRepository.findByIdAndNipPegawai(idRincianBelanja, nipPegawai)
                .orElseThrow(() -> new RincianBelanjaNotFoundException(idRincianBelanja, nipPegawai));

        return buildRincianBelanjaDetailResponse(rincianBelanja);
    }

    private Map<String, Object> buildRincianBelanjaDetailResponse(RincianBelanja rincianBelanja) {
        List<IndikatorDetailResponse> indikatorList = buildIndikatorResponses(rincianBelanja.id());

        List<SubkegiatanResponse> subkegiatanList = buildSubkegiatanResponses(rincianBelanja.id());

        RincianAnggaranCalculationResult calculationResult = buildRincianAnggaranResponses(rincianBelanja);
        List<RincianAnggaranResponse> rincianAnggaranList = calculationResult.rincianAnggaranResponses();

        List<TotalPerBulanResponse> totalPerBulanList = buildTotalPerBulanResponses(
            calculationResult.totalBobotPerBulan()
        );

        int totalKeseluruhan = calculateTotalKeseluruhan(calculationResult.totalBobotPerBulan());

        // Build main response
        RincianBelanjaDetailResponse detailResponse = RincianBelanjaDetailResponse.from(
            rincianBelanja,
            indikatorList,
            subkegiatanList,
            rincianAnggaranList,
            totalPerBulanList,
            totalKeseluruhan
        );

        // Convert to Map for controller compatibility
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("rencanaKinerja", detailResponse.toMap());

        return response;
    }

    // Helper methods for building individual response components
    private List<IndikatorDetailResponse> buildIndikatorResponses(Long rincianBelanjaId) {
        List<IndikatorBelanja> indikatorBelanjas = indikatorBelanjaService.findByRincianBelanjaId(rincianBelanjaId);

        return indikatorBelanjas.stream()
            .map(indikatorBelanja -> {
                List<TargetBelanja> targetBelanjas = targetBelanjaService.findByIndikatorBelanjaId(indikatorBelanja.id());
                List<TargetResponse> targetResponses = 
                    targetBelanjas.stream()
                        .map(TargetResponse::from)
                        .toList();
                return new IndikatorDetailResponse(
                    indikatorBelanja.id(),
                    indikatorBelanja.namaIndikatorBelanja(),
                    targetResponses
                );
            })
            .toList();
    }

    private List<SubkegiatanResponse> buildSubkegiatanResponses(Long rincianBelanjaId) {
        List<SubKegiatanRincianBelanja> subkegiatans =
            subKegiatanRincianBelanjaService.findByIdRincianBelanja(rincianBelanjaId.intValue());

        return subkegiatans.stream()
            .map(SubkegiatanResponse::from)
            .toList();
    }

    private RincianAnggaranCalculationResult buildRincianAnggaranResponses(RincianBelanja rincianBelanja) {
        Iterable<RincianAnggaran> rincianAnggarans = rincianAnggaranService.findByIdRincianAnggaranOrderByUrutan(
            rincianBelanja.id().intValue()
        );

        int[] totalBobotPerBulan = new int[12];
        boolean[] bulanMemilikiData = new boolean[12];
        List<RincianAnggaranResponse> rincianAnggaranResponses = new ArrayList<>();

        for (RincianAnggaran rincianAnggaran : rincianAnggarans) {
            List<Map<String, Object>> pelaksanaanList =
                pelaksanaanRincianAnggaranService.buildPelaksanaanRincianAnggaranResponseList(rincianAnggaran.id().intValue());

            int jumlahBobot = calculateJumlahBobot(pelaksanaanList, totalBobotPerBulan, bulanMemilikiData);

            // Fetch kode rekening data
            List<KodeRekeningResponse> kodeRekeningList = StreamSupport.stream(
                kodeRekeningRincianAnggaranService.findByIdRincianAnggaran(rincianAnggaran.id().intValue()).spliterator(),
                false
            )
            .map(KodeRekeningResponse::from)
            .toList();

            RincianAnggaranResponse response = RincianAnggaranResponse.from(
                rincianAnggaran,
                rincianBelanja.id(),
                kodeRekeningList,
                pelaksanaanList,
                jumlahBobot
            );
            rincianAnggaranResponses.add(response);
        }

        return new RincianAnggaranCalculationResult(rincianAnggaranResponses, totalBobotPerBulan, bulanMemilikiData);
    }

    private int calculateJumlahBobot(
        List<Map<String, Object>> pelaksanaanList,
        int[] totalBobotPerBulan,
        boolean[] bulanMemilikiData
    ) {
        int jumlahBobot = 0;

        for (Map<String, Object> pelaksanaanAnggaran : pelaksanaanList) {
            Integer bobot = (Integer) pelaksanaanAnggaran.get("bobot");
            Integer bulan = (Integer) pelaksanaanAnggaran.get("bulan");
            Object idObj = pelaksanaanAnggaran.get("id");
            jumlahBobot += bobot;

            if (bulan != null && bulan >= 1 && bulan <= 12) {
                totalBobotPerBulan[bulan - 1] += bobot;
                if (idObj != null && !idObj.equals(0)) {
                    bulanMemilikiData[bulan - 1] = true;
                }
            }
        }

        return jumlahBobot;
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

    // Helper record untuk hasil kalkulasi
    private record RincianAnggaranCalculationResult(
        List<RincianAnggaranResponse> rincianAnggaranResponses,
        int[] totalBobotPerBulan,
        boolean[] bulanMemilikiData
    ) {}

    @Transactional
    public ResponseEntity<Map<String, Object>> ubahRincianBelanja(Long id, RincianBelanjaRequest request) {
        RincianBelanja existRincianBelanja = rincianBelanjaRepository.findById(id)
                .orElseThrow(() -> new RincianBelanjaNotFoundException(id));

        RincianBelanja updatedRincianBelanja = new RincianBelanja(
                id,
                request.idSumberDana(),
                request.idRencanaKinerja(),
                request.idRencanaAksi(),
                request.kodeOpd(),
                request.namaOpd(),
                request.tahun(),
                request.statusRincianBelanja(),
                request.nipPegawai(),
                request.namaPegawai(),
                request.sumberDana(),
                request.rencanaKinerja(),
                request.rencanaAksi(),
                null,
                existRincianBelanja.createdDate(),
                existRincianBelanja.lastModifiedDate()
        );

        RincianBelanja savedRincianBelanja = rincianBelanjaRepository.save(updatedRincianBelanja); 

        // indikator response
        List<cc.kertaskerja.bontang.rincianbelanja.web.response.IndikatorUpdateResponse> indikatorResponses =
            buildIndikatorWithTargetsForUpdate(savedRincianBelanja, request.indikatorList());

        cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaUpdateResponse response =
            cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaUpdateResponse.from(
                savedRincianBelanja,
                indikatorResponses
            );

        return ResponseEntity.ok(response.toMap());
    }

    // Method untuk memproses update target
    private List<TargetBelanja> buildTargetBelanjasForUpdate(
            Long indikatorBelanjaId,
            List<RincianBelanjaRequest.TargetData> targetList) {

        List<TargetBelanja> targetBelanjasList = new ArrayList<>();

        List<TargetBelanja> existingTargetBelanjas = targetBelanjaService.findByIndikatorBelanjaId(indikatorBelanjaId);

        // Hapus target yang tidak ada di request
        Set<Long> requestTargetBelanjaIds = new HashSet<>();
        if (targetList != null) {
            requestTargetBelanjaIds = targetList.stream()
                .map(RincianBelanjaRequest.TargetData::idTarget)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }

        for (TargetBelanja existing : existingTargetBelanjas) {
            if (!requestTargetBelanjaIds.contains(existing.id())) {
                targetBelanjaService.hapusTargetBelanja(existing.id());
            }
        }

        // Proses target dari request
        if (targetList != null && !targetList.isEmpty()) {
            for (RincianBelanjaRequest.TargetData targetData : targetList) {
                TargetBelanja targetBelanja;

                if (targetData.idTarget() != null) {
                    TargetBelanja existing = targetBelanjaService.detailTargetBelanjaById(targetData.idTarget());
                    targetBelanja = new TargetBelanja(
                        existing.id(),
                        targetData.namaTarget() != null ? targetData.namaTarget() : existing.namaTargetBelanja(),
                        targetData.satuan() != null ? targetData.satuan() : existing.satuan(),
                        existing.indikatorBelanjaId(),
                        existing.createdDate(),
                        null
                    );
                    targetBelanja = targetBelanjaService.ubahTargetBelanja(existing.id(), new cc.kertaskerja.bontang.targetbelanja.web.TargetBelanjaRequest(
                        existing.id(),
                        targetBelanja.namaTargetBelanja(),
                        targetBelanja.satuan(),
                        targetBelanja.indikatorBelanjaId()
                    ));
                } else {
                    targetBelanja = targetBelanjaService.tambahTargetBelanja(
                        new TargetBelanjaRequest(
                            null,
                            targetData.namaTarget(),
                            targetData.satuan(),
                            indikatorBelanjaId
                        )
                    );
                }

                targetBelanjasList.add(targetBelanja);
            }
        }

        return targetBelanjasList;
    }

    // Method untuk memproses update indikator
    private List<cc.kertaskerja.bontang.rincianbelanja.web.response.IndikatorUpdateResponse> buildIndikatorWithTargetsForUpdate(
            RincianBelanja savedRincianBelanja,
            List<RincianBelanjaRequest.IndikatorData> indikatorBelanjaList) {

        List<cc.kertaskerja.bontang.rincianbelanja.web.response.IndikatorUpdateResponse> indikatorResponseList = new ArrayList<>();

        List<IndikatorBelanja> existingIndikatorBelanjas = indikatorBelanjaService.findByRincianBelanjaId(savedRincianBelanja.id());

        Set<Long> requestIndikatorBelanjaIds = new HashSet<>();
        if (indikatorBelanjaList != null) {
            requestIndikatorBelanjaIds = indikatorBelanjaList.stream()
                .map(RincianBelanjaRequest.IndikatorData::idIndikator)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }

        for (IndikatorBelanja existing : existingIndikatorBelanjas) {
            if (!requestIndikatorBelanjaIds.contains(existing.id())) {
                // Hapus indikator dan target terkait
                indikatorBelanjaService.hapusIndikatorBelanja(existing.id());
            }
        }

        // Proses indikator dari request
        if (indikatorBelanjaList != null && !indikatorBelanjaList.isEmpty()) {
            for (RincianBelanjaRequest.IndikatorData indikatorData : indikatorBelanjaList) {
                IndikatorBelanja indikatorBelanja;

                if (indikatorData.idIndikator() != null) {
                    IndikatorBelanja existing = indikatorBelanjaService.detailIndikatorBelanjaById(indikatorData.idIndikator());
                    IndikatorBelanjaRequest indikatorRequest = new IndikatorBelanjaRequest(
                        existing.id(),
                        indikatorData.namaIndikator() != null ? indikatorData.namaIndikator() : existing.namaIndikatorBelanja(),
                        existing.rincianBelanjaId()
                    );
                    indikatorBelanja = indikatorBelanjaService.ubahIndikatorBelanja(existing.id(), indikatorRequest);
                } else {
                    indikatorBelanja = indikatorBelanjaService.tambahIndikatorBelanja(
                        new IndikatorBelanjaRequest(
                            null,
                            indikatorData.namaIndikator(),
                            savedRincianBelanja.id()
                        )
                    );
                }

                // Proses target
                List<TargetBelanja> targetBelanjas = buildTargetBelanjasForUpdate(
                    indikatorBelanja.id(),
                    indikatorData.targetList()
                );

                indikatorResponseList.add(cc.kertaskerja.bontang.rincianbelanja.web.response.IndikatorUpdateResponse.from(
                    indikatorBelanja,
                    targetBelanjas
                ));
            }
        }

        return indikatorResponseList;
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> tambahRincianBelanja(RincianBelanjaRequest request) {
        RincianBelanja rincianBelanja = RincianBelanja.of(
                null,
                request.idSumberDana(),
                request.idRencanaKinerja(),
                request.idRencanaAksi(),
                request.kodeOpd(),
                request.namaOpd(),
                request.tahun(),
                request.statusRincianBelanja(),
                request.nipPegawai(),
                request.namaPegawai(),
                request.sumberDana(),
                request.rencanaKinerja(),
                request.rencanaAksi(),
                null
        );
        RincianBelanja savedRincianBelanja = rincianBelanjaRepository.save(rincianBelanja);

        // Cek jika data provider dibutuhkan
        return buildResponse(savedRincianBelanja, request.indikatorList());
    }

    // Untuk method tambahRincianBelanja
    private ResponseEntity<Map<String, Object>> buildResponse(
            RincianBelanja savedRincianBelanja,
            List<RincianBelanjaRequest.IndikatorData> indikatorList) {

        List<IndikatorCreateResponse> indikatorBelanjaResponses = buildIndikatorWithTargetsForCreate(
            indikatorList,
            savedRincianBelanja.id()
        );

        RincianBelanjaCreateResponse createResponse = RincianBelanjaCreateResponse.from(
            savedRincianBelanja,
            indikatorBelanjaResponses
        );

        return ResponseEntity.ok(createResponse.toMap());
    }

    private List<IndikatorCreateResponse> buildIndikatorWithTargetsForCreate(
            List<RincianBelanjaRequest.IndikatorData> indikatorList,
            Long rincianBelanjaId) {

        if (indikatorList == null || indikatorList.isEmpty()) {
            return List.of();
        }
        return indikatorList.stream()
            .map(indikatorData -> {
                IndikatorBelanja indikatorBelanja = indikatorBelanjaService.tambahIndikatorBelanja(
                    new IndikatorBelanjaRequest(
                        null,
                        indikatorData.namaIndikator(),
                        rincianBelanjaId
                    )
                );

                List<TargetBelanja> targetBelanjas = buildTargetBelanjasForCreate(
                    indikatorBelanja.id(),
                    indikatorData.targetList()
                );

                return IndikatorCreateResponse.from(
                    indikatorBelanja,
                    targetBelanjas
                );
            })
            .toList();
    }

    private List<TargetBelanja> buildTargetBelanjasForCreate(
            Long indikatorBelanjaId,
            List<RincianBelanjaRequest.TargetData> targetList) {

        if (targetList == null || targetList.isEmpty()) {
            return List.of();
        }

        return targetList.stream()
            .map(targetData -> targetBelanjaService.tambahTargetBelanja(
                new TargetBelanjaRequest(
                    null,
                    targetData.namaTarget(),
                    targetData.satuan(),
                    indikatorBelanjaId
                )
            ))
            .toList();
    }

    public void hapusRincianBelanja(Long id) {

        rincianBelanjaRepository.deleteById(id);
    }
}
