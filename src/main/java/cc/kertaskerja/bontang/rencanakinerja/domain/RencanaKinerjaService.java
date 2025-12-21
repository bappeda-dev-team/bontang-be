package cc.kertaskerja.bontang.rencanakinerja.domain;

import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaNotFoundException;
import cc.kertaskerja.bontang.rencanakinerja.web.RencanaKinerjaRequest;
import cc.kertaskerja.bontang.target.domain.TargetService;
import cc.kertaskerja.bontang.target.domain.Target;
import cc.kertaskerja.bontang.indikator.domain.IndikatorService;
import cc.kertaskerja.bontang.indikator.domain.Indikator;

@Service
public class RencanaKinerjaService {
    private RencanaKinerjaRepository rencanaKinerjaRepository;
    private TargetService targetService;
    private IndikatorService indikatorService;

    public RencanaKinerjaService(RencanaKinerjaRepository rencanaKinerjaRepository, TargetService targetService, IndikatorService indikatorService) {
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.targetService = targetService;
        this.indikatorService = indikatorService;
    }

    public Iterable<RencanaKinerja> findAll() {
        return rencanaKinerjaRepository.findAll();
    }

    public Map<String, Object> findByNipPegawaiAndKodeOpdAndTahun(String nipPegawai, String kodeOpd, Integer tahun) {
        RencanaKinerja rencanaKinerja = rencanaKinerjaRepository.findByNipPegawaiAndKodeOpdAndTahun(nipPegawai, kodeOpd, tahun)
                .orElseThrow(() -> new RencanaKinerjaNotFoundException(nipPegawai, kodeOpd, tahun));
        
        return buildRencanaKinerjaDetailResponse(rencanaKinerja);
    }

    // Untuk method findByNipPegawaiAndKodeOpdAndTahun
    private Map<String, Object> buildRencanaKinerjaDetailResponse(RencanaKinerja rencanaKinerja) {
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> rencanaKinerjaResponse = new LinkedHashMap<>();
        
        // Build rencana kinerja response
        rencanaKinerjaResponse.put("nama_rencana_kinerja", rencanaKinerja.rencanaKinerja());
        rencanaKinerjaResponse.put("tahun", rencanaKinerja.tahun().toString());
        rencanaKinerjaResponse.put("status_rencana_kinerja", rencanaKinerja.statusRencanaKinerja());
        
        // Build OPD object
        Map<String, Object> operasionalDaerah = new LinkedHashMap<>();
        operasionalDaerah.put("kode_opd", rencanaKinerja.kodeOpd());
        operasionalDaerah.put("nama_opd", rencanaKinerja.namaOpd());
        rencanaKinerjaResponse.put("operasional_daerah", operasionalDaerah);
        
        rencanaKinerjaResponse.put("nip", rencanaKinerja.nipPegawai());
        rencanaKinerjaResponse.put("nama_pegawai", rencanaKinerja.namaPegawai());
        
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
            indikatorResponse.put("nama_indikator", indikator.namaIndikator());
            indikatorResponse.put("targets", targetsResponseList);
            indikatorResponseList.add(indikatorResponse);
        }
        
        rencanaKinerjaResponse.put("indikator", indikatorResponseList);
        
        response.put("rencana_kinerja", rencanaKinerjaResponse);
        return response;
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> tambahRencanaKinerja(RencanaKinerjaRequest request) {
        RencanaKinerja rencanaKinerja = RencanaKinerja.of(
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
        Map<String, Object> rencanaKinerjaResponse = new LinkedHashMap<>();
        
        // Build rencana kinerja response
        if (indikatorList != null && !indikatorList.isEmpty()) {
            rencanaKinerjaResponse.put("nama_rencana_kinerja", savedRencanaKinerja.rencanaKinerja());
            rencanaKinerjaResponse.put("tahun", savedRencanaKinerja.tahun().toString());
            rencanaKinerjaResponse.put("status_rencana_kinerja", savedRencanaKinerja.statusRencanaKinerja());
            
            // Build OPD object
            Map<String, Object> operasionalDaerah = new LinkedHashMap<>();
            operasionalDaerah.put("kode_opd", savedRencanaKinerja.kodeOpd());
            operasionalDaerah.put("nama_opd", savedRencanaKinerja.namaOpd());
            rencanaKinerjaResponse.put("operasional_daerah", operasionalDaerah);
            
            rencanaKinerjaResponse.put("nip", savedRencanaKinerja.nipPegawai());
            rencanaKinerjaResponse.put("nama_pegawai", savedRencanaKinerja.namaPegawai());
            
            // Process indikator list
            List<Map<String, Object>> indikatorResponseList = new ArrayList<>();
            for (RencanaKinerjaRequest.IndikatorData indikatorData : indikatorList) {
                Indikator savedIndikator = indikatorService.tambahIndikator(
                        indikatorData.namaIndikator(),
                        savedRencanaKinerja.id()
                );

                List<Map<String, Object>> targetsResponseList = new ArrayList<>();
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
                        targetsResponseList.add(targetResponse);
                    }
                }

                // Build indikator response
                Map<String, Object> indikatorResponse = new LinkedHashMap<>();
                indikatorResponse.put("id_indikator", savedIndikator.id());
                indikatorResponse.put("nama_indikator", savedIndikator.namaIndikator());
                indikatorResponse.put("targets", targetsResponseList);
                indikatorResponseList.add(indikatorResponse);
            }
            
            rencanaKinerjaResponse.put("indikator", indikatorResponseList);
        } else {
            rencanaKinerjaResponse.put("id_rencana_kinerja", savedRencanaKinerja.id().toString());
            rencanaKinerjaResponse.put("nama_rencana_kinerja", savedRencanaKinerja.rencanaKinerja());
            rencanaKinerjaResponse.put("tahun", savedRencanaKinerja.tahun().toString());
            rencanaKinerjaResponse.put("status_rencana_kinerja", savedRencanaKinerja.statusRencanaKinerja());
            
            Map<String, Object> operasionalDaerah = new LinkedHashMap<>();
            operasionalDaerah.put("kode_opd", savedRencanaKinerja.kodeOpd());
            operasionalDaerah.put("nama_opd", savedRencanaKinerja.namaOpd());
            rencanaKinerjaResponse.put("operasional_daerah", operasionalDaerah);
            
            rencanaKinerjaResponse.put("pegawai_id", savedRencanaKinerja.nipPegawai());
            rencanaKinerjaResponse.put("nama_pegawai", savedRencanaKinerja.namaPegawai());
        }
        
        response.put("rencana_kinerja", rencanaKinerjaResponse);
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

        return buildResponse(savedRencanaKinerja, request.indikatorList());
    }

    public void hapusRencanaKinerja(Long id) {

        rencanaKinerjaRepository.deleteById(id);
    }
}
