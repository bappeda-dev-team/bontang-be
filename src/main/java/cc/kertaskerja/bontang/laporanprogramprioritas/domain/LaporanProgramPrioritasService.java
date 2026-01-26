package cc.kertaskerja.bontang.laporanprogramprioritas.domain;

import cc.kertaskerja.bontang.laporanprogramprioritas.domain.exception.LaporanProgramPrioritasNotFoundException;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.LaporanProgramPrioritasDataResponse;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.PelaksanaLaporanResponse;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.RencanaKinerjaLaporanResponse;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.TahapanPelaksanaanResponse;
import cc.kertaskerja.bontang.programprioritas.domain.ProgramPrioritas;
import cc.kertaskerja.bontang.programprioritas.domain.exception.ProgramPrioritasNotFoundException;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaran;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRencanaKinerja;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRepository;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRencanaKinerjaRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.exception.RencanaKinerjaNotFoundException;
import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain.PelaksanaanRepository;
import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanja;
import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanjaRepository;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerjaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LaporanProgramPrioritasService {
    private final LaporanProgramPrioritasRepository laporanProgramPrioritasRepository;
    private final ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository;
    private final ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository;
    private final RencanaKinerjaRepository rencanaKinerjaEntityRepository;
    private final SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository;
    private final PelaksanaanRepository pelaksanaanRepository;
    private final RincianBelanjaRepository rincianBelanjaRepository;

    public LaporanProgramPrioritasService(
            LaporanProgramPrioritasRepository laporanProgramPrioritasRepository,
            ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository,
            ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository,
            RencanaKinerjaRepository rencanaKinerjaEntityRepository,
            SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository,
            PelaksanaanRepository pelaksanaanRepository,
            RincianBelanjaRepository rincianBelanjaRepository
    ) {
        this.laporanProgramPrioritasRepository = laporanProgramPrioritasRepository;
        this.programPrioritasAnggaranRepository = programPrioritasAnggaranRepository;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.rencanaKinerjaEntityRepository = rencanaKinerjaEntityRepository;
        this.subKegiatanRencanaKinerjaRepository = subKegiatanRencanaKinerjaRepository;
        this.pelaksanaanRepository = pelaksanaanRepository;
        this.rincianBelanjaRepository = rincianBelanjaRepository;
    }

    public LaporanProgramPrioritasDataResponse getLaporan(
            Long idProgramPrioritas,
            String kodeOpd,
            String namaOpd,
            Integer tahun
    ) {
        // 1. Ambil data ProgramPrioritas
        ProgramPrioritas programPrioritas = laporanProgramPrioritasRepository.findById(idProgramPrioritas)
                .orElseThrow(() -> new ProgramPrioritasNotFoundException(idProgramPrioritas));

        // 2. Ambil semua ProgramPrioritasAnggaran untuk program prioritas ini
        Iterable<ProgramPrioritasAnggaran> programs =
                programPrioritasAnggaranRepository.findByIdProgramPrioritas(idProgramPrioritas);

        // 3. Group pelaksana berdasarkan NIP
        Map<String, PelaksanaLaporanResponse> pelaksanaMap = new LinkedHashMap<>();

        for (ProgramPrioritasAnggaran program : programs) {
            String nip = program.nip();
            String namaPegawai = getNamaPegawaiByNip(nip, kodeOpd, tahun);

            // 4. Ambil semua rencana kinerja untuk program prioritas anggaran ini
            Iterable<ProgramPrioritasAnggaranRencanaKinerja> rencanaKinerjaList =
                    rencanaKinerjaRepository.findByIdProgramPrioritasAnggaran(program.id());

            List<RencanaKinerjaLaporanResponse> rencanaKinerjaResponses = new ArrayList<>();

            for (ProgramPrioritasAnggaranRencanaKinerja rkRelasi : rencanaKinerjaList) {
                // 5. Ambil data RencanaKinerja
                RencanaKinerja rencanaKinerja = rencanaKinerjaEntityRepository
                        .findById(rkRelasi.idRencanaKinerja())
                        .orElse(null);

                if (rencanaKinerja != null) {
                    // 6. Ambil data SubKegiatanRencanaKinerja
                    List<SubKegiatanRencanaKinerja> subKegiatanList =
                            subKegiatanRencanaKinerjaRepository.findByIdRekin(
                                    rencanaKinerja.id().intValue()
                            );

                    // 7. Hitung total anggaran (pagu)
                    Integer pagu = 0;
                    String kodeSubkegiatan = null;
                    String namaSubkegiatan = null;

                    if (!subKegiatanList.isEmpty()) {
                        SubKegiatanRencanaKinerja subKegiatan = subKegiatanList.get(0);
                        kodeSubkegiatan = subKegiatan.kodeSubKegiatan();
                        namaSubkegiatan = subKegiatan.namaSubKegiatan();

                        List<RincianBelanja> rincianBelanjaList =
                                rincianBelanjaRepository.findByIdSubkegiatanRencanaKinerja(subKegiatan.id());
                        pagu = rincianBelanjaList.stream()
                                .mapToInt(RincianBelanja::totalAnggaran)
                                .sum();
                    }

                    // 8. Hitung tahapan pelaksanaan (TW)
                    Integer tw1 = pelaksanaanRepository.sumBobotTW1ByIdRekin(
                            rencanaKinerja.id().intValue()
                    );
                    Integer tw2 = pelaksanaanRepository.sumBobotTW2ByIdRekin(
                            rencanaKinerja.id().intValue()
                    );
                    Integer tw3 = pelaksanaanRepository.sumBobotTW3ByIdRekin(
                            rencanaKinerja.id().intValue()
                    );
                    Integer tw4 = pelaksanaanRepository.sumBobotTW4ByIdRekin(
                            rencanaKinerja.id().intValue()
                    );

                    TahapanPelaksanaanResponse tahapanPelaksanaan =
                            new TahapanPelaksanaanResponse(tw1, tw2, tw3, tw4);

                    // 9. Buat RencanaKinerjaLaporanResponse
                    RencanaKinerjaLaporanResponse rkResponse =
                            new RencanaKinerjaLaporanResponse(
                                    rencanaKinerja.id(),
                                    rencanaKinerja.rencanaKinerja(),
                                    rencanaKinerja.namaPegawai(),
                                    rencanaKinerja.nipPegawai(),
                                    kodeSubkegiatan,
                                    namaSubkegiatan,
                                    pagu,
                                    tahapanPelaksanaan
                            );

                    rencanaKinerjaResponses.add(rkResponse);
                }
            }

            // 10. Tambahkan ke pelaksanaMap
            if (!pelaksanaMap.containsKey(nip)) {
                pelaksanaMap.put(nip, new PelaksanaLaporanResponse(
                        namaPegawai,
                        nip,
                        rencanaKinerjaResponses
                ));
            } else {
                // Merge rencana kinerja jika pelaksana sudah ada
                PelaksanaLaporanResponse existing = pelaksanaMap.get(nip);
                List<RencanaKinerjaLaporanResponse> merged = new ArrayList<>(existing.rencana_kinerjas());
                merged.addAll(rencanaKinerjaResponses);
                pelaksanaMap.put(nip, new PelaksanaLaporanResponse(
                        existing.nama_pelaksana(),
                        existing.nip_pelaksana(),
                        merged
                ));
            }
        }

        // 11. Buat LaporanProgramPrioritasDataResponse
        return new LaporanProgramPrioritasDataResponse(
                programPrioritas.id(),
                programPrioritas.programPrioritas(),
                tahun,
                kodeOpd,
                namaOpd,
                new ArrayList<>(pelaksanaMap.values()),
                "-"
        );
    }

    public List<LaporanProgramPrioritasDataResponse> getLaporanBatch(
            List<Long> idProgramPrioritasList,
            String kodeOpd,
            String namaOpd,
            Integer tahun
    ) {
        return idProgramPrioritasList.stream()
                .map(id -> getLaporan(id, kodeOpd, namaOpd, tahun))
                .toList();
    }

    private String getNamaPegawaiByNip(String nip, String kodeOpd, Integer tahun) {
        List<RencanaKinerja> rencanaKinerjaList =
                rencanaKinerjaEntityRepository.findByNipPegawaiAndKodeOpdAndTahun(nip, kodeOpd, tahun);
        return rencanaKinerjaList.stream()
                .findFirst()
                .map(RencanaKinerja::namaPegawai)
                .orElse("-");
    }
}
