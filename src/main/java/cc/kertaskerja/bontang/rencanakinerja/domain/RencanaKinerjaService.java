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

@Service
public class RencanaKinerjaService {
    private RencanaKinerjaRepository rencanaKinerjaRepository;
    private TargetService targetService;
    private IndikatorService indikatorService;
    private RencanaAksiService rencanaAksiService;
    private PelaksanaanService pelaksanaanService;
    private SubKegiatanRencanaKinerjaService subKegiatanRencanaKinerjaService;
    private DasarHukumService dasarHukumService;
    private GambaranUmumService gambaranUmumService;

    public RencanaKinerjaService(RencanaKinerjaRepository rencanaKinerjaRepository,
                           TargetService targetService,
                           IndikatorService indikatorService,
                           RencanaAksiService rencanaAksiService,
                           PelaksanaanService pelaksanaanService,
                           SubKegiatanRencanaKinerjaService subKegiatanRencanaKinerjaService,
                           DasarHukumService dasarHukumService,
                           GambaranUmumService gambaranUmumService) {
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.targetService = targetService;
        this.indikatorService = indikatorService;
        this.rencanaAksiService = rencanaAksiService;
        this.pelaksanaanService = pelaksanaanService;
        this.subKegiatanRencanaKinerjaService = subKegiatanRencanaKinerjaService;
        this.dasarHukumService = dasarHukumService;
        this.gambaranUmumService = gambaranUmumService;
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
        List<Map<String, Object>> rencanaKinerjaList = new ArrayList<>();
        
        for (RencanaKinerja rencanaKinerja : rencanaKinerjas) {
            Map<String, Object> rencanaKinerjaData = new LinkedHashMap<>();
            
            rencanaKinerjaData.put("id_rencana_kinerja", rencanaKinerja.id());
            rencanaKinerjaData.put("rencanaKinerja", rencanaKinerja.rencanaKinerja());
            rencanaKinerjaData.put("kodeOpd", rencanaKinerja.kodeOpd());
            rencanaKinerjaData.put("nipPegawai", rencanaKinerja.nipPegawai());
            rencanaKinerjaData.put("createdBy", rencanaKinerja.createdBy());
            rencanaKinerjaData.put("tahun", rencanaKinerja.tahun());
            rencanaKinerjaData.put("statusRencanaKinerja", rencanaKinerja.statusRencanaKinerja());
            rencanaKinerjaData.put("namaOpd", rencanaKinerja.namaOpd());
            rencanaKinerjaData.put("namaPegawai", rencanaKinerja.namaPegawai());
            rencanaKinerjaData.put("sumberDana", rencanaKinerja.sumberDana());
            rencanaKinerjaData.put("keterangan", rencanaKinerja.keterangan());
            
            List<Map<String, Object>> indikatorList = new ArrayList<>();
            List<Indikator> indikators = indikatorService.findByRencanaKinerjaId(rencanaKinerja.id());
            
            for (Indikator indikator : indikators) {
                List<Map<String, Object>> targetList = new ArrayList<>();
                List<Target> targets = targetService.findByIndikatorId(indikator.id());
                
                for (Target target : targets) {
                    Map<String, Object> targetResponse = new LinkedHashMap<>();
                    rencanaKinerjaData.put("id_target", target.id());
                    targetResponse.put("target", target.target());
                    targetResponse.put("satuan", target.satuan());
                    targetList.add(targetResponse);
                }
                
                Map<String, Object> indikatorResponse = new LinkedHashMap<>();
                rencanaKinerjaData.put("id_indikator", indikator.id());
                indikatorResponse.put("namaIndikator", indikator.namaIndikator());
                indikatorResponse.put("targetList", targetList);
                indikatorList.add(indikatorResponse);
            }
            
            rencanaKinerjaData.put("indikatorList", indikatorList);
            rencanaKinerjaList.add(rencanaKinerjaData);
        }
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("rencana_kinerja", rencanaKinerjaList);
        
        return response;
    }

    // Untuk method findByNipPegawaiAndKodeOpdAndTahun
    private Map<String, Object> buildRencanaKinerjaDetailResponse(RencanaKinerja rencanaKinerja) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> rencanaKinerjaResponse = new LinkedHashMap<>();
        
        // Build rencana kinerja response
        rencanaKinerjaResponse.put("id_rencana_kinerja", rencanaKinerja.id());
        rencanaKinerjaResponse.put("namaRencanaKinerja", rencanaKinerja.rencanaKinerja());
        rencanaKinerjaResponse.put("tahun", rencanaKinerja.tahun().toString());
        rencanaKinerjaResponse.put("statusRencanaKinerja", rencanaKinerja.statusRencanaKinerja());
        rencanaKinerjaResponse.put("id_sumber_dana", rencanaKinerja.idSumberDana());
        rencanaKinerjaResponse.put("sumberDana", rencanaKinerja.sumberDana());
        
        // Build OPD object
        Map<String, Object> operasionalDaerah = new LinkedHashMap<>();
        operasionalDaerah.put("kodeOpd", rencanaKinerja.kodeOpd());
        operasionalDaerah.put("namaOpd", rencanaKinerja.namaOpd());
        rencanaKinerjaResponse.put("operasionalDaerah", operasionalDaerah);
        
        rencanaKinerjaResponse.put("nip", rencanaKinerja.nipPegawai());
        rencanaKinerjaResponse.put("namaPegawai", rencanaKinerja.namaPegawai());
        
        // Process indikator list
        List<Map<String, Object>> indikatorResponseList = new ArrayList<>();
        List<Indikator> indikators = indikatorService.findByRencanaKinerjaId(rencanaKinerja.id());
        
        for (Indikator indikator : indikators) {
            List<Map<String, Object>> targetsResponseList = new ArrayList<>();
            List<Target> targets = targetService.findByIndikatorId(indikator.id());
            
            for (Target target : targets) {
                Map<String, Object> targetResponse = new LinkedHashMap<>();
                targetResponse.put("id_target", target.id());
                targetResponse.put("target", target.target());
                targetResponse.put("satuan", target.satuan());
                targetsResponseList.add(targetResponse);
            }
            
            // Build indikator response
            Map<String, Object> indikatorResponse = new LinkedHashMap<>();
            indikatorResponse.put("id_indikator", indikator.id());
            indikatorResponse.put("namaIndikator", indikator.namaIndikator());
            indikatorResponse.put("targets", targetsResponseList);
            indikatorResponseList.add(indikatorResponse);
        }
        
        rencanaKinerjaResponse.put("indikator", indikatorResponseList);

        // Process subkegiatan list
        List<Map<String, Object>> subkegiatanResponseList = new ArrayList<>();
        List<SubKegiatanRencanaKinerja> subkegiatans = subKegiatanRencanaKinerjaService.findByIdRekin(rencanaKinerja.id().intValue());
        
        for (SubKegiatanRencanaKinerja subkegiatan : subkegiatans) {
            Map<String, Object> subkegiatanResponse = new LinkedHashMap<>();
            subkegiatanResponse.put("id", subkegiatan.id());
            subkegiatanResponse.put("rencana_kinerja_id", subkegiatan.idRekin());
            subkegiatanResponse.put("kodeSubkegiatan", subkegiatan.kodeSubKegiatan());
            subkegiatanResponse.put("namaSubKegiatan", subkegiatan.namaSubKegiatan());
            subkegiatanResponseList.add(subkegiatanResponse);
        }
        
        rencanaKinerjaResponse.put("subkegiatan", subkegiatanResponseList);

        // Process dasar hukum list
        List<Map<String, Object>> dasarHukumResponseList = new ArrayList<>();
        Iterable<DasarHukum> dasarHukums = dasarHukumService.findByIdRencanaKinerja(rencanaKinerja.id());
        
        for (DasarHukum dasarHukum : dasarHukums) {
            Map<String, Object> dasarHukumResponse = new LinkedHashMap<>();
            dasarHukumResponse.put("id", dasarHukum.id());
            dasarHukumResponse.put("rencana_kinerja_id", rencanaKinerja.id());
            dasarHukumResponse.put("kodeOpd", dasarHukum.kodeOpd());
            dasarHukumResponse.put("uraian", dasarHukum.uraian());
            dasarHukumResponse.put("peraturanTerkait", dasarHukum.peraturanTerkait());
            dasarHukumResponseList.add(dasarHukumResponse);
        }
        
        rencanaKinerjaResponse.put("dasarHukum", dasarHukumResponseList);

        // Process gambaran umum list
        List<Map<String, Object>> gambaranUmumResponseList = new ArrayList<>();
        Iterable<GambaranUmum> gambaranUmums = gambaranUmumService.findByIdRencanaKinerja(rencanaKinerja.id());
        
        for (GambaranUmum gambaranUmum : gambaranUmums) {
            Map<String, Object> gambaranUmumResponse = new LinkedHashMap<>();
            gambaranUmumResponse.put("id", gambaranUmum.id());
            gambaranUmumResponse.put("rencana_kinerja_id", rencanaKinerja.id());
            gambaranUmumResponse.put("kodeOpd", gambaranUmum.kodeOpd());
            gambaranUmumResponse.put("uraian", gambaranUmum.uraian());
            gambaranUmumResponse.put("gambaranUmum", gambaranUmum.gambaranUmum());
            gambaranUmumResponseList.add(gambaranUmumResponse);
        }
        
        rencanaKinerjaResponse.put("gambaranUmum", gambaranUmumResponseList);

        // Process rencana aksi list
        List<Map<String, Object>> rencanaAksiResponseList = new ArrayList<>();
        Iterable<RencanaAksi> rencanaAksis = rencanaAksiService.findByIdRekinOrderByUrutan(rencanaKinerja.id().intValue());

        int[] totalBobotPerBulan = new int[12];
        boolean[] bulanMemilikiData = new boolean[12];

        for (RencanaAksi rencanaAksi : rencanaAksis) {
            List<Map<String, Object>> pelaksanaanList = pelaksanaanService.buildPelaksanaanResponseList(
                rencanaAksi.id().intValue()
            );

            int jumlahBobot = 0;
            for (Map<String, Object> pelaksanaan : pelaksanaanList) {
                Integer bobot = (Integer) pelaksanaan.get("bobot");
                Integer bulan = (Integer) pelaksanaan.get("bulan");
                Object idObj = pelaksanaan.get("id");
                jumlahBobot += bobot;

                if (bulan != null && bulan >= 1 && bulan <= 12) {
                    totalBobotPerBulan[bulan - 1] += bobot;
                    // Tandai bulan ini memiliki data pelaksanaan (id > 0)
                    if (idObj != null && !idObj.equals(0)) {
                        bulanMemilikiData[bulan - 1] = true;
                    }
                }
            }

            // Build rencana aksi response
            Map<String, Object> rencanaAksiResponse = new LinkedHashMap<>();
            rencanaAksiResponse.put("id", rencanaAksi.id());
            rencanaAksiResponse.put("rencana_kinerja_id", rencanaKinerja.id());
            rencanaAksiResponse.put("urutan", rencanaAksi.urutan());
            rencanaAksiResponse.put("namaRencanaAksi", rencanaAksi.namaRencanaAksi());
            rencanaAksiResponse.put("pelaksanaan", pelaksanaanList);
            rencanaAksiResponse.put("jumlahBobot", jumlahBobot);

            rencanaAksiResponseList.add(rencanaAksiResponse);
        }

        // Build total_per_bulan response
        List<Map<String, Object>> totalPerBulanList = new ArrayList<>();
        for (int bulan = 1; bulan <= 12; bulan++) {
            Map<String, Object> totalBulan = new LinkedHashMap<>();
            totalBulan.put("bulan", bulan);
            totalBulan.put("totalBobot", totalBobotPerBulan[bulan - 1]);
            totalPerBulanList.add(totalBulan);
        }

        rencanaKinerjaResponse.put("rencanaAksi", rencanaAksiResponseList);
        rencanaKinerjaResponse.put("totalPerBulan", totalPerBulanList);

        int totalKeseluruhan = 0;
        for (int bobot : totalBobotPerBulan) {
            totalKeseluruhan += bobot;
        }

        int waktuDibutuhkan = 0;
        for (boolean hasData : bulanMemilikiData) {
            if (hasData) {
                waktuDibutuhkan++;
            }
        }

        rencanaKinerjaResponse.put("totalKeseluruhan", totalKeseluruhan);
        rencanaKinerjaResponse.put("waktuDibutuhkan", waktuDibutuhkan);

        response.put("rencanaKinerja", rencanaKinerjaResponse);
        return response;
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> tambahRencanaKinerja(RencanaKinerjaRequest request) {
        RencanaKinerja rencanaKinerja = RencanaKinerja.of(
                request.idSumberDana(),
                request.rencanaKinerja(),
                request.kodeOpd(),
                request.nipPegawai(),
                request.createdBy(),
                request.tahun(),
                request.statusRencanaKinerja(),
                request.namaOpd(),
                request.namaPegawai(),
                request.sumberDana(),
                request.keterangan()
        );
        RencanaKinerja savedRencanaKinerja = rencanaKinerjaRepository.save(rencanaKinerja);

        // Cek jika data provider dibutuhkan
        return buildResponse(savedRencanaKinerja, request.indikatorList());
    }

    // Untuk method tambahRencanaKinerja
    private ResponseEntity<Map<String, Object>> buildResponse(
            RencanaKinerja savedRencanaKinerja,
            List<RencanaKinerjaRequest.IndikatorData> indikatorList) {
        
        Map<String, Object> response = new LinkedHashMap<>();
        
        response.put("id_rencana_kinerja", savedRencanaKinerja.id());
        response.put("idSumberDana", savedRencanaKinerja.idSumberDana());
        response.put("rencanaKinerja", savedRencanaKinerja.rencanaKinerja());
        response.put("kodeOpd", savedRencanaKinerja.kodeOpd());
        response.put("nipPegawai", savedRencanaKinerja.nipPegawai());
        response.put("createdBy", savedRencanaKinerja.createdBy());
        response.put("tahun", savedRencanaKinerja.tahun());
        response.put("statusRencanaKinerja", savedRencanaKinerja.statusRencanaKinerja());
        response.put("namaOpd", savedRencanaKinerja.namaOpd());
        response.put("namaPegawai", savedRencanaKinerja.namaPegawai());
        response.put("sumberDana", savedRencanaKinerja.sumberDana());
        response.put("keterangan", savedRencanaKinerja.keterangan());
        
        // Process indikator list
        List<Map<String, Object>> indikatorListResponse = new ArrayList<>();
        if (indikatorList != null && !indikatorList.isEmpty()) {
            for (RencanaKinerjaRequest.IndikatorData indikatorData : indikatorList) {
                Indikator savedIndikator = indikatorService.tambahIndikator(
                        indikatorData.namaIndikator(),
                        savedRencanaKinerja.id()
                );

                List<Map<String, Object>> targetListResponse = new ArrayList<>();
                List<RencanaKinerjaRequest.TargetData> targetList = indikatorData.targetList();
                if (targetList != null && !targetList.isEmpty()) {
                    for (RencanaKinerjaRequest.TargetData targetData : targetList) {
                        Target savedTarget = targetService.tambahTarget(
                                targetData.target(),
                                targetData.satuan(),
                                savedIndikator.id()
                        );
                        
                        Map<String, Object> targetResponse = new LinkedHashMap<>();
                        targetResponse.put("id_target", savedTarget.id());
                        targetResponse.put("target", savedTarget.target());
                        targetResponse.put("satuan", savedTarget.satuan());
                        targetListResponse.add(targetResponse);
                    }
                }

                // Build indikator response
                Map<String, Object> indikatorResponse = new LinkedHashMap<>();
                indikatorResponse.put("id_indikator", savedIndikator.id());
                indikatorResponse.put("namaIndikator", savedIndikator.namaIndikator());
                indikatorResponse.put("targetList", targetListResponse);
                indikatorListResponse.add(indikatorResponse);
            }
        }
        
        response.put("indikatorList", indikatorListResponse);
        
        return ResponseEntity.ok(response);
    }

    // Response tanpa indikator
    private ResponseEntity<Map<String, Object>> buildResponseWithoutIndikator(RencanaKinerja savedRencanaKinerja) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> rencanaKinerjaResponse = buildRencanaKinerjaResponse(savedRencanaKinerja, true);
        response.put("rencana_kinerja", rencanaKinerjaResponse);

        return ResponseEntity.ok(response);
    }

    // Response dengan indikator
    private List<Map<String, Object>> processIndikatorList(
            RencanaKinerja savedRencanaKinerja,
            List<RencanaKinerjaRequest.IndikatorData> indikatorList) {

        List<Map<String, Object>> indikatorResponseList = new ArrayList<>();

        for (RencanaKinerjaRequest.IndikatorData indikatorData : indikatorList) {
            Indikator savedIndikator = indikatorService.tambahIndikator(
                    indikatorData.namaIndikator(),
                    savedRencanaKinerja.id()
            );

            Map<String, Object> indikatorResponse = buildIndikatorResponse(savedIndikator, indikatorData.targetList());
            indikatorResponseList.add(indikatorResponse);
        }

        return indikatorResponseList;
    }

    // Build indikator response
    private Map<String, Object> buildIndikatorResponse(Indikator savedIndikator, List<RencanaKinerjaRequest.TargetData> targetList) {
        List<Map<String, Object>> targetsResponseList = new ArrayList<>();

        // Proses target jika ada
        if (targetList != null && !targetList.isEmpty()) {
            for (RencanaKinerjaRequest.TargetData targetData : targetList) {
                Target savedTarget = targetService.tambahTarget(
                        targetData.target(),
                        targetData.satuan(),
                        savedIndikator.id()
                );
                
                Map<String, Object> targetResponse = new LinkedHashMap<>();
                targetResponse.put("id_target", savedTarget.id());
                targetResponse.put("target", savedTarget.target());
                targetResponse.put("satuan", savedTarget.satuan());
                targetsResponseList.add(targetResponse);
            }
        }

        Map<String, Object> indikatorResponse = new LinkedHashMap<>();
        indikatorResponse.put("id_indikator", savedIndikator.id());
        indikatorResponse.put("nama_indikator", savedIndikator.namaIndikator());
        indikatorResponse.put("targets", targetsResponseList);

        return indikatorResponse;
    }

    // Method untuk memproses update target
    private List<Map<String, Object>> processTargetForUpdate(
            Long indikatorId,
            List<RencanaKinerjaRequest.TargetData> targetList) {
        
        List<Map<String, Object>> targetsResponseList = new ArrayList<>();
        
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
                
                Map<String, Object> targetResponse = new LinkedHashMap<>();
                targetResponse.put("id_target", target.id());
                targetResponse.put("target", target.target());
                targetResponse.put("satuan", target.satuan());
                targetsResponseList.add(targetResponse);
            }
        }
        
        return targetsResponseList;
    }

    // Method untuk memproses update indikator
    private List<Map<String, Object>> processIndikatorForUpdate(
            RencanaKinerja savedRencanaKinerja,
            List<RencanaKinerjaRequest.IndikatorData> indikatorList) {
        
        List<Map<String, Object>> indikatorResponseList = new ArrayList<>();
        
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
                List<Map<String, Object>> targetsResponse = processTargetForUpdate(
                    indikator.id(),
                    indikatorData.targetList()
                );
                
                Map<String, Object> indikatorResponse = new LinkedHashMap<>();
                indikatorResponse.put("id_indikator", indikator.id());
                indikatorResponse.put("nama_indikator", indikator.namaIndikator());
                indikatorResponse.put("targets", targetsResponse);
                indikatorResponseList.add(indikatorResponse);
            }
        }
        
        return indikatorResponseList;
    }

    private Map<String, Object> buildRencanaKinerjaResponse(RencanaKinerja rencanaKinerja, boolean includeId) {
        Map<String, Object> response = new LinkedHashMap<>();

        if (includeId) {
            response.put("id_rencana_kinerja", rencanaKinerja.id().toString());
        }

        response.put("nama_rencana_kinerja", rencanaKinerja.rencanaKinerja());
        response.put("tahun", rencanaKinerja.tahun().toString());
        response.put("status_rencana_kinerja", rencanaKinerja.statusRencanaKinerja());

        Map<String, Object> operasionalDaerah = new LinkedHashMap<>();
        operasionalDaerah.put("kode_opd", rencanaKinerja.kodeOpd());
        operasionalDaerah.put("nama_opd", rencanaKinerja.namaOpd());
        response.put("operasional_daerah", operasionalDaerah);

        response.put("pegawai_id", rencanaKinerja.nipPegawai());
        response.put("nama_pegawai", rencanaKinerja.namaPegawai());

        return response;
    }

    
    @Transactional
    public ResponseEntity<Map<String, Object>> ubahRencanaKinerja(Long id, RencanaKinerjaRequest request) {
        RencanaKinerja existingRencanaKinerja = rencanaKinerjaRepository.findById(id)
                .orElseThrow(() -> new RencanaKinerjaNotFoundException(id));
        
        RencanaKinerja updatedRencanaKinerja = new RencanaKinerja(
                id,
                request.idSumberDana() != null ? request.idSumberDana() : existingRencanaKinerja.idSumberDana(),
                request.rencanaKinerja(),
                request.kodeOpd() != null ? request.kodeOpd() : existingRencanaKinerja.kodeOpd(),
                request.nipPegawai() != null ? request.nipPegawai() : existingRencanaKinerja.nipPegawai(),
                request.createdBy() != null ? request.createdBy() : existingRencanaKinerja.createdBy(),
                request.tahun() != null ? request.tahun() : existingRencanaKinerja.tahun(),
                request.statusRencanaKinerja() != null ? request.statusRencanaKinerja() : existingRencanaKinerja.statusRencanaKinerja(),
                request.namaOpd() != null ? request.namaOpd() : existingRencanaKinerja.namaOpd(),
                request.namaPegawai() != null ? request.namaPegawai() : existingRencanaKinerja.namaPegawai(),
                request.sumberDana() != null ? request.sumberDana() : existingRencanaKinerja.sumberDana(),
                request.keterangan() != null ? request.keterangan() : existingRencanaKinerja.keterangan(),
                existingRencanaKinerja.createdDate(),
                existingRencanaKinerja.lastModifiedDate()
        );
        
        RencanaKinerja savedRencanaKinerja = rencanaKinerjaRepository.save(updatedRencanaKinerja);

        // Build response untuk update
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id_rencana_kinerja", savedRencanaKinerja.id());
        response.put("idSumberDana", savedRencanaKinerja.idSumberDana());
        response.put("rencanaKinerja", savedRencanaKinerja.rencanaKinerja());
        response.put("kodeOpd", savedRencanaKinerja.kodeOpd());
        response.put("nipPegawai", savedRencanaKinerja.nipPegawai());
        response.put("createdBy", savedRencanaKinerja.createdBy());
        response.put("tahun", savedRencanaKinerja.tahun());
        response.put("statusRencanaKinerja", savedRencanaKinerja.statusRencanaKinerja());
        response.put("namaOpd", savedRencanaKinerja.namaOpd());
        response.put("namaPegawai", savedRencanaKinerja.namaPegawai());
        response.put("sumberDana", savedRencanaKinerja.sumberDana());
        response.put("keterangan", savedRencanaKinerja.keterangan());
        response.put("indikatorList", processIndikatorForUpdate(savedRencanaKinerja, request.indikatorList()));
        
        return ResponseEntity.ok(response);
    }

    public void hapusRencanaKinerja(Long id) {

        rencanaKinerjaRepository.deleteById(id);
    }
}
