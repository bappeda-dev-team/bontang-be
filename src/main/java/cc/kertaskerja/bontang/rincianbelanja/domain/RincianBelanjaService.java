package cc.kertaskerja.bontang.rincianbelanja.domain;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.rincianbelanja.domain.exception.RincianBelanjaNotFoundException;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaResponse;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaResponse.RincianBelanjaItem;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaResponse.Indikator;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaResponse.Target;
import cc.kertaskerja.bontang.rincianbelanja.web.response.RincianBelanjaResponse.RencanaAksi;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaService;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaNotFoundException;
import cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksiService;

@Service
public class RincianBelanjaService {
    private final RincianBelanjaRepository rincianBelanjaRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final RencanaKinerjaRepository rencanaKinerjaRepository;
    private final RencanaAksiService rencanaAksiService;

    private RincianBelanjaService(
            RincianBelanjaRepository rincianBelanjaRepository,
            RencanaKinerjaService rencanaKinerjaService,
            RencanaKinerjaRepository rencanaKinerjaRepository,
            RencanaAksiService rencanaAksiService) {
        this.rincianBelanjaRepository = rincianBelanjaRepository;
        this.rencanaKinerjaService = rencanaKinerjaService;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.rencanaAksiService = rencanaAksiService;
    }

    public List<RincianBelanja> findBySubkegiatanRencanaKinerjaId(Long idSubkegiatanRencanaKinerja) {
        List<RincianBelanja> rincianBelanjaList = rincianBelanjaRepository.findByIdSubkegiatanRencanaKinerja(idSubkegiatanRencanaKinerja);
        if (rincianBelanjaList.isEmpty()) {
            throw new RincianBelanjaNotFoundException(idSubkegiatanRencanaKinerja);
        }
        return rincianBelanjaList;
    }

    public RincianBelanjaResponse findByNipPegawaiKodeOpdAndTahun(String nipPegawai, String kodeOpd, Integer tahun) {
        // Step 1: Cari RencanaKinerja berdasarkan parameter
        List<RencanaKinerja> rencanaKinerjaList = rencanaKinerjaRepository
            .findByNipPegawaiAndKodeOpdAndTahun(nipPegawai, kodeOpd, tahun);
        
        if (rencanaKinerjaList.isEmpty()) {
            throw new RencanaKinerjaNotFoundException(nipPegawai, kodeOpd, tahun);
        }
        
        RencanaKinerja rencanaKinerja = rencanaKinerjaList.get(0);
        
        // Step 2: Ambil detail lengkap dari rencana kinerja
        Map<String, Object> rencanaKinerjaDetail = 
            rencanaKinerjaService.findDetailByIdAndNipPegawai(
                rencanaKinerja.id(), 
                nipPegawai
            );
        
        // Step 3: Fetch actual RincianBelanja records from database
        List<RincianBelanja> rincianBelanjaRecords = 
            rincianBelanjaRepository.findByNipPegawaiAndKodeOpdAndTahun(
                nipPegawai, kodeOpd, tahun
            );
        
        // Step 4: Transformasi ke RincianBelanjaResponse
        return buildRincianBelanjaResponseFromRencanaKinerja(
            rencanaKinerjaDetail,
            nipPegawai,
            kodeOpd,
            tahun,
            rincianBelanjaRecords
        );
    }
    
    private RincianBelanjaResponse buildRincianBelanjaResponseFromRencanaKinerja(
            Map<String, Object> rencanaKinerjaDetail,
            String nipPegawai,
            String kodeOpd,
            Integer tahun,
            List<RincianBelanja> rincianBelanjaRecords
    ) {
        Map<String, Object> rencanaKinerjaData = (Map<String, Object>) rencanaKinerjaDetail.get("rencanaKinerja");
        
        String namaPegawai = (String) rencanaKinerjaData.get("namaPegawai");
        
        // Get sub kegiatan list
        List<Map<String, Object>> subkegiatanList = 
            (List<Map<String, Object>>) rencanaKinerjaData.get("subKegiatan");
        
        // Get indikator list
        List<Map<String, Object>> indikatorList = 
            (List<Map<String, Object>>) rencanaKinerjaData.get("indikator");
        
        // Get rencana aksi list
        List<Map<String, Object>> rencanaAksiList = 
            (List<Map<String, Object>>) rencanaKinerjaData.get("rencanaAksi");
        
        // Build RincianBelanjaItem list
        List<RincianBelanjaItem> rincianBelanjaItems = new ArrayList<>();
        
        for (Map<String, Object> subkegiatan : subkegiatanList) {
            Long idSubKegiatanRencanaKinerja = convertToLong(subkegiatan.get("id"));
            String kodeSubkegiatan = (String) subkegiatan.get("kodeSubKegiatan");
            String namaSubkegiatan = (String) subkegiatan.get("namaSubKegiatan");
            
            // Build indikator list for this sub kegiatan
            List<Indikator> indikators = new ArrayList<>();
            for (Map<String, Object> indikatorData : indikatorList) {
                String idIndikator = convertToString(indikatorData.get("id_indikator"));
                String namaIndikator = (String) indikatorData.get("namaIndikator");
                
                List<Target> targets = new ArrayList<>();
                List<Map<String, Object>> targetsData = 
                    (List<Map<String, Object>>) indikatorData.get("targets");
                
                if (targetsData != null) {
                    for (Map<String, Object> targetData : targetsData) {
                        String idTarget = convertToString(targetData.get("id_target"));
                        String target = (String) targetData.get("target");
                        String satuan = (String) targetData.get("satuan");
                        targets.add(new Target(idTarget, target, satuan));
                    }
                }
                
                indikators.add(new Indikator(idIndikator, namaIndikator, targets));
            }
            
            // Build rencana aksi list for this sub kegiatan with actual anggaran from database
            List<RencanaAksi> rencanaAksis = new ArrayList<>();
            for (Map<String, Object> rencanaAksiData : rencanaAksiList) {
                String idRencanaAksi = convertToString(rencanaAksiData.get("id"));
                String namaRencanaAksi = (String) rencanaAksiData.get("namaRencanaAksi");
                
                // Find actual anggaran from RincianBelanja records
                Integer actualAnggaran = 0;
                try {
                    Long idRencanaAksiLong = convertToLong(rencanaAksiData.get("id"));
                    for (RincianBelanja rb : rincianBelanjaRecords) {
                        if (rb.idRencanaAksi() != null && 
                            rb.idRencanaAksi().equals(idRencanaAksiLong)) {
                            actualAnggaran = rb.anggaran();
                            break;
                        }
                    }
                } catch (Exception e) {
                    actualAnggaran = 0;
                }
                
                rencanaAksis.add(new RencanaAksi(idRencanaAksi, namaRencanaAksi, actualAnggaran));
            }
            
            // Calculate total anggaran for this subkegiatan from rencana aksi
            int subkegiatanTotalAnggaran = rencanaAksis.stream()
                .mapToInt(RencanaAksi::anggaran)
                .sum();
            
            rincianBelanjaItems.add(new RincianBelanjaItem(
                convertToString(idSubKegiatanRencanaKinerja),
                namaSubkegiatan,
                indikators,
                subkegiatanTotalAnggaran,
                rencanaAksis
            ));
        }
        
        // Calculate total anggaran from actual RincianBelanja records
        Integer totalAnggaran = rincianBelanjaRecords.stream()
            .mapToInt(RincianBelanja::anggaran)
            .sum();
        
        // Get kode and nama sub kegiatan from first item if available
        String kodeSubkegiatan = null;
        String namaSubkegiatan = null;
        if (!rincianBelanjaItems.isEmpty()) {
            Map<String, Object> firstSubkegiatan = subkegiatanList.get(0);
            kodeSubkegiatan = (String) firstSubkegiatan.get("kodeSubKegiatan");
            namaSubkegiatan = (String) firstSubkegiatan.get("namaSubKegiatan");
        }
        
        return new RincianBelanjaResponse(
            nipPegawai,
            namaPegawai,
            kodeSubkegiatan,
            namaSubkegiatan,
            totalAnggaran,
            rincianBelanjaItems
        );
    }
    
    private String convertToString(Object value) {
        if (value == null) return null;
        return value.toString();
    }
    
    private Long convertToLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        return Long.parseLong(value.toString());
    }
    
    private List<RincianBelanjaItem> groupBySubkegiatan(List<RincianBelanja> rincianBelanjaList) {
        return rincianBelanjaList.stream()
            .collect(Collectors.groupingBy(
                rb -> rb.idSubkegiatanRencanaKinerja(),
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    this::createRincianBelanjaItem
                )
            ))
            .values()
            .stream()
            .toList();
    }
    
    private RincianBelanjaItem createRincianBelanjaItem(List<RincianBelanja> items) {
        RincianBelanja first = items.get(0);
        Integer totalAnggaran = items.stream()
            .mapToInt(RincianBelanja::anggaran)
            .sum();
        
        List<RencanaAksi> rencanaAksiList = items.stream()
            .map(rb -> new RencanaAksi(
                rb.idRencanaAksi() != null ? String.valueOf(rb.idRencanaAksi()) : null,
                rb.rencanaAksi(),
                rb.anggaran()
            ))
            .toList();
        
        List<Indikator> indikatorList = items.stream()
            .filter(rb -> rb.indikator() != null && !rb.indikator().isBlank())
            .map(rb -> new Indikator(
                first.idSubkegiatanRencanaKinerja() != null ? String.valueOf(first.idSubkegiatanRencanaKinerja()) : null,
                rb.indikator(),
                rb.target() != null ? List.of(new Target(
                    first.idSubkegiatanRencanaKinerja() != null ? String.valueOf(first.idSubkegiatanRencanaKinerja()) : null,
                    rb.target(),
                    rb.satuan()
                )) : List.of()
            ))
            .distinct()
            .toList();
        
        return new RincianBelanjaItem(
            first.idSubkegiatanRencanaKinerja() != null ? String.valueOf(first.idSubkegiatanRencanaKinerja()) : null,
            first.namaSubkegiatan(),
            indikatorList,
            totalAnggaran,
            rencanaAksiList
        );
    }

    public RincianBelanja upsertRincianBelanja(Long idRencanaAksi, Integer anggaran) {
        // Check if record exists
        Optional<RincianBelanja> existingRecord = rincianBelanjaRepository.findByIdRencanaAksi(idRencanaAksi);
        
        if (existingRecord.isPresent()) {
            // UPDATE EXISTING RECORD
            RincianBelanja existing = existingRecord.get();
            
            // Fetch indikator, target, and satuan from RencanaKinerja
            String indikator = existing.indikator();
            String target = existing.target();
            String satuan = existing.satuan();
            
            if (existing.nipPegawai() != null) {
                try {
                    // Get RencanaAksi to find the RencanaKinerja ID
                    cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi rencanaAksi = 
                        rencanaAksiService.detailRencanaAksiById(idRencanaAksi);
                    
                    if (rencanaAksi != null && rencanaAksi.idRekin() != null) {
                        // Fetch RencanaKinerja detail to get indikator data
                        Map<String, Object> rencanaKinerjaDetail = 
                            rencanaKinerjaService.findDetailByIdAndNipPegawai(
                                rencanaAksi.idRekin().longValue(),
                                existing.nipPegawai()
                            );
                        
                        // Extract indikator data from RencanaKinerja detail
                        if (rencanaKinerjaDetail != null) {
                            Map<String, Object> rencanaKinerjaData = 
                                (Map<String, Object>) rencanaKinerjaDetail.get("rencanaKinerja");
                            
                            if (rencanaKinerjaData != null) {
                                List<Map<String, Object>> indikatorList = 
                                    (List<Map<String, Object>>) rencanaKinerjaData.get("indikator");
                                
                                // Get first indikator's data (assuming one indikator per subkegiatan)
                                if (indikatorList != null && !indikatorList.isEmpty()) {
                                    Map<String, Object> indikatorData = indikatorList.get(0);
                                    indikator = (String) indikatorData.get("namaIndikator");
                                    
                                    // Get target data
                                    List<Map<String, Object>> targetList = 
                                        (List<Map<String, Object>>) indikatorData.get("target");
                                    if (targetList != null && !targetList.isEmpty()) {
                                        Map<String, Object> targetData = targetList.get(0);
                                        target = (String) targetData.get("target");
                                        satuan = (String) targetData.get("satuan");
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // If fetching indikator data fails, use existing values
                    System.err.println("Error fetching indikator data: " + e.getMessage());
                }
            }
            
            // Update existing record with fetched indikator data
            RincianBelanja updated = new RincianBelanja(
                existing.id(),
                existing.idSubkegiatanRencanaKinerja(),
                existing.idRencanaAksi(),
                existing.nipPegawai(),
                existing.namaPegawai(),
                existing.kodeOpd(),
                existing.namaOpd(),
                existing.tahun(),
                existing.kodeSubkegiatan(),
                existing.namaSubkegiatan(),
                indikator,
                target,
                satuan,
                existing.sumberDana(),
                existing.rencanaAksi(),
                existing.kodeRekening(),
                existing.namaRekening(),
                anggaran,
                existing.totalAnggaran(),
                existing.createdDate(),
                existing.lastModifiedDate()
            );
            
            RincianBelanja savedRecord = rincianBelanjaRepository.save(updated);
            
            // Calculate total anggaran by summing all anggaran for this subkegiatan
            if (existing.idSubkegiatanRencanaKinerja() != null) {
                List<RincianBelanja> allRecords = 
                    rincianBelanjaRepository.findByIdSubkegiatanRencanaKinerja(
                        existing.idSubkegiatanRencanaKinerja()
                    );
                
                int totalAnggaran = allRecords.stream()
                    .mapToInt(RincianBelanja::anggaran)
                    .sum();
                
                // Update all records with new total anggaran
                for (RincianBelanja record : allRecords) {
                    RincianBelanja recordWithTotal = new RincianBelanja(
                        record.id(),
                        record.idSubkegiatanRencanaKinerja(),
                        record.idRencanaAksi(),
                        record.nipPegawai(),
                        record.namaPegawai(),
                        record.kodeOpd(),
                        record.namaOpd(),
                        record.tahun(),
                        record.kodeSubkegiatan(),
                        record.namaSubkegiatan(),
                        record.indikator(),
                        record.target(),
                        record.satuan(),
                        record.sumberDana(),
                        record.rencanaAksi(),
                        record.kodeRekening(),
                        record.namaRekening(),
                        record.anggaran(),
                        totalAnggaran,
                        record.createdDate(),
                        record.lastModifiedDate()
                    );
                    rincianBelanjaRepository.save(recordWithTotal);
                }
            }
            
            return savedRecord;
        } else {
            // CREATE NEW RECORD
            try {
                // Get RencanaAksi details
                cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi rencanaAksi = 
                    rencanaAksiService.detailRencanaAksiById(idRencanaAksi);
                
                if (rencanaAksi == null) {
                    return null; // RencanaAksi not found
                }
                
                // Get RencanaKinerja to fetch details
                RencanaKinerja rencanaKinerja = rencanaKinerjaRepository.findById(
                    rencanaAksi.idRekin().longValue()
                ).orElse(null);
                
                if (rencanaKinerja == null) {
                    return null; // RencanaKinerja not found
                }
                
                // Fetch indikator data from RencanaKinerja
                String indikator = null;
                String target = null;
                String satuan = null;
                
                Map<String, Object> rencanaKinerjaDetail = 
                    rencanaKinerjaService.findDetailByIdAndNipPegawai(
                        rencanaAksi.idRekin().longValue(),
                        rencanaKinerja.nipPegawai()
                    );
                
                if (rencanaKinerjaDetail != null) {
                    Map<String, Object> rencanaKinerjaData = 
                        (Map<String, Object>) rencanaKinerjaDetail.get("rencanaKinerja");
                    
                    if (rencanaKinerjaData != null) {
                        List<Map<String, Object>> indikatorList = 
                            (List<Map<String, Object>>) rencanaKinerjaData.get("indikator");
                        
                        // Get first indikator's data
                        if (indikatorList != null && !indikatorList.isEmpty()) {
                            Map<String, Object> indikatorData = indikatorList.get(0);
                            indikator = (String) indikatorData.get("namaIndikator");
                            
                            // Get target data
                            List<Map<String, Object>> targetList = 
                                (List<Map<String, Object>>) indikatorData.get("target");
                            if (targetList != null && !targetList.isEmpty()) {
                                Map<String, Object> targetData = targetList.get(0);
                                target = (String) targetData.get("target");
                                satuan = (String) targetData.get("satuan");
                            }
                        }
                    }
                }
                
                // Create new RincianBelanja record
                RincianBelanja newRecord = new RincianBelanja(
                    null, // id will be auto-generated
                    null, // idSubkegiatanRencanaKinerja - will be set if available
                    idRencanaAksi,
                    rencanaKinerja.nipPegawai(),
                    rencanaKinerja.namaPegawai(),
                    rencanaKinerja.kodeOpd(),
                    rencanaKinerja.namaOpd(),
                    rencanaKinerja.tahun(),
                    null, // kodeSubkegiatan
                    null, // namaSubkegiatan
                    indikator,
                    target,
                    satuan,
                    rencanaKinerja.sumberDana(),
                    rencanaAksi.namaRencanaAksi(),
                    null, // kodeRekening
                    null, // namaRekening
                    anggaran,
                    anggaran, // totalAnggaran starts as the same as anggaran
                    null, // createdDate
                    null // lastModifiedDate
                );
                
                RincianBelanja savedRecord = rincianBelanjaRepository.save(newRecord);
                
                // Update total anggaran if there are other records
                // (This will be calculated when more records are added)
                
                return savedRecord;
            } catch (Exception e) {
                System.err.println("Error creating new RincianBelanja record: " + e.getMessage());
                return null;
            }
        }
    }

}
