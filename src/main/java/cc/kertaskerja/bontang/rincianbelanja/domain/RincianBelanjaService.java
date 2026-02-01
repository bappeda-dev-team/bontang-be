package cc.kertaskerja.bontang.rincianbelanja.domain;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
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
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerjaRepository;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;

@Service
public class RincianBelanjaService {
    private final RincianBelanjaRepository rincianBelanjaRepository;
    private final RencanaKinerjaService rencanaKinerjaService;
    private final RencanaKinerjaRepository rencanaKinerjaRepository;
    private final RencanaAksiService rencanaAksiService;
    private final SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository;

    private RincianBelanjaService(
            RincianBelanjaRepository rincianBelanjaRepository,
            RencanaKinerjaService rencanaKinerjaService,
            RencanaKinerjaRepository rencanaKinerjaRepository,
            RencanaAksiService rencanaAksiService,
            SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository) {
        this.rincianBelanjaRepository = rincianBelanjaRepository;
        this.rencanaKinerjaService = rencanaKinerjaService;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.rencanaAksiService = rencanaAksiService;
        this.subKegiatanRencanaKinerjaRepository = subKegiatanRencanaKinerjaRepository;
    }

    public List<RincianBelanja> findBySubkegiatanRencanaKinerjaId(Long idSubkegiatanRencanaKinerja) {
        List<RincianBelanja> rincianBelanjaList = rincianBelanjaRepository.findByIdSubkegiatanRencanaKinerja(idSubkegiatanRencanaKinerja);
        if (rincianBelanjaList.isEmpty()) {
            throw new RincianBelanjaNotFoundException(idSubkegiatanRencanaKinerja);
        }
        return rincianBelanjaList;
    }

    /**
     * Mengisi daftar entri RincianBelanjaResponse untuk semua catatan rencana kinerja yang sesuai.
     */
    public List<RincianBelanjaResponse> findByNipPegawaiKodeOpdAndTahun(String nipPegawai, String kodeOpd, Integer tahun) {
        // Step 1: Cari RencanaKinerja berdasarkan parameter
        List<RencanaKinerja> rencanaKinerjaList = rencanaKinerjaRepository
            .findByNipPegawaiAndKodeOpdAndTahun(nipPegawai, kodeOpd, tahun);
        
        if (rencanaKinerjaList.isEmpty()) {
            throw new RencanaKinerjaNotFoundException(nipPegawai, kodeOpd, tahun);
        }
        
        // Step 2: Ambil catatan RincianBelanja yang sebenarnya dari database.
        List<RincianBelanja> rincianBelanjaRecords = 
            rincianBelanjaRepository.findByNipPegawaiAndKodeOpdAndTahun(
                nipPegawai, kodeOpd, tahun
            );

        List<RincianBelanjaResponse> responses = new ArrayList<>();

        for (RencanaKinerja rencanaKinerja : rencanaKinerjaList) {
            Map<String, Object> rencanaKinerjaDetail =
                rencanaKinerjaService.findDetailByIdAndNipPegawai(
                    rencanaKinerja.id(),
                    nipPegawai
                );

            responses.addAll(buildRincianBelanjaResponsesFromRencanaKinerja(
                rencanaKinerjaDetail,
                nipPegawai,
                kodeOpd,
                tahun,
                rincianBelanjaRecords
            ));
        }

        return responses;
    }
    
    /**
     * Manage payload subkegiatan RencanaKinerjaDetail.
     */
    private List<RincianBelanjaResponse> buildRincianBelanjaResponsesFromRencanaKinerja(
            Map<String, Object> rencanaKinerjaDetail,
            String nipPegawai,
            String kodeOpd,
            Integer tahun,
            List<RincianBelanja> rincianBelanjaRecords
    ) {
        Map<String, Object> rencanaKinerjaData = (Map<String, Object>) rencanaKinerjaDetail.get("rencanaKinerja");
        
        String namaPegawai = (String) rencanaKinerjaData.get("namaPegawai");
        
        List<Map<String, Object>> subkegiatanList = 
            (List<Map<String, Object>>) rencanaKinerjaData.get("subKegiatan");
        
        List<Map<String, Object>> indikatorList = 
            (List<Map<String, Object>>) rencanaKinerjaData.get("indikator");
        
        List<Map<String, Object>> rencanaAksiList = 
            (List<Map<String, Object>>) rencanaKinerjaData.get("rencanaAksi");

        List<Indikator> indikatorDetails = parseIndikatorDetails(indikatorList);
        List<ActionDetail> rencanaAksiDetails = parseRencanaAksiDetails(rencanaAksiList);
        String namaRencanaKinerja = (String) rencanaKinerjaData.get("namaRencanaKinerja");
        
        Map<Long, List<RincianBelanja>> recordsBySubkegiatan = rincianBelanjaRecords.stream()
            .filter(rb -> rb.idSubkegiatanRencanaKinerja() != null)
            .collect(Collectors.groupingBy(RincianBelanja::idSubkegiatanRencanaKinerja));

        List<RincianBelanjaResponse> responses = new ArrayList<>();

        if (subkegiatanList != null) {
            for (Map<String, Object> subkegiatan : subkegiatanList) {
                Long idSubKegiatanRencanaKinerja = convertToLong(subkegiatan.get("id"));
                String kodeSubkegiatan = (String) subkegiatan.get("kodeSubKegiatan");
                String namaSubkegiatan = (String) subkegiatan.get("namaSubKegiatan");
        List<RincianBelanja> subkegiatanRecords = recordsBySubkegiatan.getOrDefault(
            idSubKegiatanRencanaKinerja, List.of()
        );

        List<RincianBelanjaItem> rincianBelanjaItems = buildRincianBelanjaItemsForSubk(
            idSubKegiatanRencanaKinerja,
            namaSubkegiatan,
            namaRencanaKinerja,
            indikatorDetails,
            rencanaAksiDetails,
            subkegiatanRecords
        );

                Integer totalAnggaran = subkegiatanRecords.stream()
                    .mapToInt(RincianBelanja::anggaran)
                    .sum();

                responses.add(new RincianBelanjaResponse(
                    nipPegawai,
                    namaPegawai,
                    kodeSubkegiatan,
                    namaSubkegiatan,
                    totalAnggaran,
                    rincianBelanjaItems
                ));
            }
        }

        return responses;
    }

    private List<RincianBelanjaItem> buildRincianBelanjaItemsForSubk(
            Long idSubkegiatanRencanaKinerja,
            String namaSubkegiatan,
            String namaRencanaKinerja,
            List<Indikator> indikatorDetails,
            List<ActionDetail> rencanaAksiDetails,
            List<RincianBelanja> subkegiatanRecords
    ) {
        List<RencanaAksi> rencanaAksis = buildRencanaAksiList(rencanaAksiDetails, subkegiatanRecords);
        List<Indikator> indikators = indikatorDetails != null ? indikatorDetails : List.of();

        int subkegiatanTotalAnggaran = subkegiatanRecords.stream()
            .mapToInt(RincianBelanja::anggaran)
            .sum();

        return List.of(new RincianBelanjaItem(
            convertToString(idSubkegiatanRencanaKinerja),
            namaRencanaKinerja != null ? namaRencanaKinerja : namaSubkegiatan,
            indikators,
            subkegiatanTotalAnggaran,
            rencanaAksis
        ));
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

    /**
     * Konversi data map indikator DTOs setiap kali response dibuat.
     */
    private List<Indikator> parseIndikatorDetails(List<Map<String, Object>> indikatorList) {
        if (indikatorList == null) {
            return List.of();
        }

        return indikatorList.stream()
            .map(indikatorData -> {
                String idIndikator = convertToString(indikatorData.get("id_indikator"));
                String namaIndikator = (String) indikatorData.get("namaIndikator");
                List<Map<String, Object>> targetsData =
                    (List<Map<String, Object>>) indikatorData.get("targets");

                return new Indikator(idIndikator, namaIndikator, parseTargetDetails(targetsData));
            })
            .toList();
    }

    private List<Target> parseTargetDetails(List<Map<String, Object>> targetsData) {
        if (targetsData == null) {
            return List.of();
        }

        return targetsData.stream()
            .map(targetData -> new Target(
                convertToString(targetData.get("id_target")),
                (String) targetData.get("target"),
                (String) targetData.get("satuan")
            ))
            .toList();
    }

    /**
     * Normalisasi rencana anksi dengan anggaran.
     */
    private List<ActionDetail> parseRencanaAksiDetails(List<Map<String, Object>> rencanaAksiList) {
        if (rencanaAksiList == null) {
            return List.of();
        }

        return rencanaAksiList.stream()
            .map(data -> new ActionDetail(
                convertToString(data.get("id")),
                convertToLong(data.get("id")),
                (String) data.get("namaRencanaAksi")
            ))
            .toList();
    }

    /**
     * Hasil payload rencana aksi dengan jumlah anggaran .
     */
    private List<RencanaAksi> buildRencanaAksiList(
            List<ActionDetail> actionDetails,
            List<RincianBelanja> subkegiatanRecords
    ) {
        List<RencanaAksi> actions = new ArrayList<>();

        for (ActionDetail action : actionDetails) {
            Integer actualAnggaran = calculateAnggaranForAction(subkegiatanRecords, action.idLong());
            String kodeRekening = findFirstKodeRekeningForAction(subkegiatanRecords, action.idLong());
            String namaRekening = findFirstNamaRekeningForAction(subkegiatanRecords, action.idLong());
            actions.add(new RencanaAksi(action.idString(), action.nama(), actualAnggaran, kodeRekening, namaRekening));
        }

        return actions;
    }

    /**
     * Jumlahkan anggaran dari setiap rencana aksi id.
     */
    private Integer calculateAnggaranForAction(List<RincianBelanja> records, Long idRencanaAksi) {
        if (idRencanaAksi == null || records == null) {
            return 0;
        }

        return records.stream()
            .filter(rb -> idRencanaAksi.equals(rb.idRencanaAksi()))
            .mapToInt(RincianBelanja::anggaran)
            .sum();
    }

    private String findFirstKodeRekeningForAction(List<RincianBelanja> records, Long idRencanaAksi) {
        if (idRencanaAksi == null || records == null) {
            return null;
        }

        return records.stream()
            .filter(rb -> idRencanaAksi.equals(rb.idRencanaAksi()))
            .map(RincianBelanja::kodeRekening)
            .filter(kode -> kode != null && !kode.isBlank())
            .findFirst()
            .orElse(null);
    }

    private String findFirstNamaRekeningForAction(List<RincianBelanja> records, Long idRencanaAksi) {
        if (idRencanaAksi == null || records == null) {
            return null;
        }

        return records.stream()
            .filter(rb -> idRencanaAksi.equals(rb.idRencanaAksi()))
            .map(RincianBelanja::namaRekening)
            .filter(nama -> nama != null && !nama.isBlank())
            .findFirst()
            .orElse(null);
    }

    private record ActionDetail(String idString, Long idLong, String nama) {}
    
    public RincianBelanja upsertRincianBelanja(Long idRencanaAksi, Integer anggaran, String kodeRekening, String namaRekening) {
        // Cek jika ada data record rencana belanja
        Optional<RincianBelanja> existingRecord = rincianBelanjaRepository.findByIdRencanaAksi(idRencanaAksi);
        
        if (existingRecord.isPresent()) {
            RincianBelanja existing = existingRecord.get();
            String resolvedKodeRekening = kodeRekening != null ? kodeRekening : existing.kodeRekening();
            String resolvedNamaRekening = namaRekening != null ? namaRekening : existing.namaRekening();
            
            // Fetch data subkegiatan jika tidak ada
            Long idSubkegiatanRencanaKinerja = existing.idSubkegiatanRencanaKinerja();
            String kodeSubkegiatan = existing.kodeSubkegiatan();
            String namaSubkegiatan = existing.namaSubkegiatan();
            
            // Fetch data indikator, target, dan satuan dari RencanaKinerja
            String indikator = existing.indikator();
            String target = existing.target();
            String satuan = existing.satuan();
            
            if (existing.nipPegawai() != null) {
                try {
                    // Ambil RencanaAksi untuk menemukan data id RencanaKinerja
                    cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi rencanaAksi = 
                        rencanaAksiService.detailRencanaAksiById(idRencanaAksi);
                    
                    if (rencanaAksi != null && rencanaAksi.idRekin() != null) {
                        // Fetch subkegiatan data if missing
                        if (idSubkegiatanRencanaKinerja == null || 
                            kodeSubkegiatan == null || 
                            namaSubkegiatan == null) {
                            
                            List<SubKegiatanRencanaKinerja> subkegiatanList = 
                                subKegiatanRencanaKinerjaRepository.findByIdRekin(
                                    rencanaAksi.idRekin()
                                );
                            
                            if (subkegiatanList != null && !subkegiatanList.isEmpty()) {
                                SubKegiatanRencanaKinerja subkegiatan = subkegiatanList.get(0);
                                idSubkegiatanRencanaKinerja = subkegiatan.id();
                                kodeSubkegiatan = subkegiatan.kodeSubKegiatan();
                                namaSubkegiatan = subkegiatan.namaSubKegiatan();
                            }
                        }
                        
                        // Fetch RencanaKinerja detail untuk ambil data indikator
                        Map<String, Object> rencanaKinerjaDetail = 
                            rencanaKinerjaService.findDetailByIdAndNipPegawai(
                                rencanaAksi.idRekin().longValue(),
                                existing.nipPegawai()
                            );
                        
                        // Extract indikator data dari RencanaKinerja detail
                        if (rencanaKinerjaDetail != null) {
                            Map<String, Object> rencanaKinerjaData = 
                                (Map<String, Object>) rencanaKinerjaDetail.get("rencanaKinerja");
                            
                            if (rencanaKinerjaData != null) {
                                List<Map<String, Object>> indikatorList = 
                                    (List<Map<String, Object>>) rencanaKinerjaData.get("indikator");
                                
                                // Ambil data indikator pertama
                                if (indikatorList != null && !indikatorList.isEmpty()) {
                                    Map<String, Object> indikatorData = indikatorList.get(0);
                                    indikator = (String) indikatorData.get("namaIndikator");
                                    
                                    // Ambil data target
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
                    System.err.println("Proses data fetching gagal : " + e.getMessage());
                }
            }
            
            RincianBelanja updated = new RincianBelanja(
                existing.id(),
                idSubkegiatanRencanaKinerja,
                existing.idRencanaAksi(),
                existing.nipPegawai(),
                existing.namaPegawai(),
                existing.kodeOpd(),
                existing.namaOpd(),
                existing.tahun(),
                kodeSubkegiatan,
                namaSubkegiatan,
                indikator,
                target,
                satuan,
                existing.sumberDana(),
                existing.rencanaAksi(),
                resolvedKodeRekening,
                resolvedNamaRekening,
                anggaran,
                existing.totalAnggaran(),
                existing.createdDate(),
                existing.lastModifiedDate()
            );
            
            RincianBelanja savedRecord = rincianBelanjaRepository.save(updated);
            
            // Jumlahkan semua anggaran dari subkegiatan tertentu
            if (existing.idSubkegiatanRencanaKinerja() != null) {
                List<RincianBelanja> allRecords = 
                    rincianBelanjaRepository.findByIdSubkegiatanRencanaKinerja(
                        existing.idSubkegiatanRencanaKinerja()
                    );
                
                int totalAnggaran = allRecords.stream()
                    .mapToInt(RincianBelanja::anggaran)
                    .sum();
                
                // Update record dengan total anggaran yang sudah dihitung
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
            try {
                cc.kertaskerja.bontang.rencanaaksi.domain.RencanaAksi rencanaAksi = 
                    rencanaAksiService.detailRencanaAksiById(idRencanaAksi);
                
                if (rencanaAksi == null) {
                    return null;
                }
                
                // Fetch data rencana kinerja detail
                RencanaKinerja rencanaKinerja = rencanaKinerjaRepository.findById(
                    rencanaAksi.idRekin().longValue()
                ).orElse(null);
                
                if (rencanaKinerja == null) {
                    return null;
                }
                
                // Fetch subkegiatan data dari subkegiatan_rencana_kinerja
                Long idSubkegiatanRencanaKinerja = null;
                String kodeSubkegiatan = null;
                String namaSubkegiatan = null;
                
                List<SubKegiatanRencanaKinerja> subkegiatanList = 
                    subKegiatanRencanaKinerjaRepository.findByIdRekin(
                        rencanaAksi.idRekin()
                    );
                
                if (subkegiatanList != null && !subkegiatanList.isEmpty()) {
                    SubKegiatanRencanaKinerja subkegiatan = subkegiatanList.get(0);
                    idSubkegiatanRencanaKinerja = subkegiatan.id();
                    kodeSubkegiatan = subkegiatan.kodeSubKegiatan();
                    namaSubkegiatan = subkegiatan.namaSubKegiatan();
                }
                
                // Fetch indikator data dari RencanaKinerja
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
                
                RincianBelanja newRecord = RincianBelanja.of(
                    idSubkegiatanRencanaKinerja, 
                    idRencanaAksi,
                    rencanaKinerja.nipPegawai(),
                    rencanaKinerja.namaPegawai(),
                    rencanaKinerja.kodeOpd(),
                    rencanaKinerja.namaOpd(),
                    rencanaKinerja.tahun(),
                    kodeSubkegiatan,
                    namaSubkegiatan,
                    indikator,
                    target,
                    satuan,
                    null,
                    rencanaAksi.namaRencanaAksi(),
                    kodeRekening,
                    namaRekening,
                    anggaran,
                    anggaran
                );
                
                RincianBelanja savedRecord = rincianBelanjaRepository.save(newRecord);
                
                return savedRecord;
            } catch (Exception e) {
                System.err.println("Error membuat data rincian belanja baru : " + e.getMessage());
                return null;
            }
        }
    }

}
