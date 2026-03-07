package cc.kertaskerja.bontang.laporanprogramprioritas.domain;

import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.LaporanProgramPrioritasDataResponse;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.PelaksanaLaporanResponse;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.RencanaKinerjaLaporanResponse;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.TahapanPelaksanaanResponse;
import cc.kertaskerja.bontang.opd.domain.Opd;
import cc.kertaskerja.bontang.opd.domain.OpdRepository;
import cc.kertaskerja.bontang.programprioritas.domain.ProgramPrioritas;
import cc.kertaskerja.bontang.programprioritas.domain.exception.ProgramPrioritasNotFoundException;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaran;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRencanaKinerja;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRepository;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRencanaKinerjaRepository;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranNotFoundException;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaVerifikatorRepository;
import cc.kertaskerja.bontang.rencanaaksi.pelaksanaan.domain.PelaksanaanRepository;
import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanja;
import cc.kertaskerja.bontang.rincianbelanja.domain.RincianBelanjaRepository;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerja;
import cc.kertaskerja.bontang.subkegiatanrencanakinerja.domain.SubKegiatanRencanaKinerjaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LaporanProgramPrioritasService {
    private final LaporanProgramPrioritasRepository laporanProgramPrioritasRepository;
    private final ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository;
    private final ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository;
    private final RencanaKinerjaRepository rencanaKinerjaEntityRepository;
    private final SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository;
    private final PelaksanaanRepository pelaksanaanRepository;
    private final RincianBelanjaRepository rincianBelanjaRepository;
    private final OpdRepository opdRepository;
    private final RencanaKinerjaVerifikatorRepository rencanaKinerjaVerifikatorRepository;

    public LaporanProgramPrioritasService(
            LaporanProgramPrioritasRepository laporanProgramPrioritasRepository,
            ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository,
            ProgramPrioritasAnggaranRencanaKinerjaRepository rencanaKinerjaRepository,
            RencanaKinerjaRepository rencanaKinerjaEntityRepository,
            SubKegiatanRencanaKinerjaRepository subKegiatanRencanaKinerjaRepository,
            PelaksanaanRepository pelaksanaanRepository,
            RincianBelanjaRepository rincianBelanjaRepository,
            OpdRepository opdRepository,
            RencanaKinerjaVerifikatorRepository rencanaKinerjaVerifikatorRepository
    ) {
        this.laporanProgramPrioritasRepository = laporanProgramPrioritasRepository;
        this.programPrioritasAnggaranRepository = programPrioritasAnggaranRepository;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.rencanaKinerjaEntityRepository = rencanaKinerjaEntityRepository;
        this.subKegiatanRencanaKinerjaRepository = subKegiatanRencanaKinerjaRepository;
        this.pelaksanaanRepository = pelaksanaanRepository;
        this.rincianBelanjaRepository = rincianBelanjaRepository;
        this.opdRepository = opdRepository;
        this.rencanaKinerjaVerifikatorRepository = rencanaKinerjaVerifikatorRepository;
    }

    private LaporanProgramPrioritasDataResponse getLaporan(
            Long idProgramPrioritasAnggaran,
            Integer tahun,
            boolean isLevel2,
            Set<Long> allowedRencanaKinerjaIds
    ) {
        // 1. Ambil data ProgramPrioritasAnggaran berdasarkan id
        ProgramPrioritasAnggaran programPrioritasAnggaran = programPrioritasAnggaranRepository.findById(idProgramPrioritasAnggaran)
                .orElseThrow(() -> new ProgramPrioritasAnggaranNotFoundException(idProgramPrioritasAnggaran));

        // 2. Ambil data ProgramPrioritas menggunakan foreign key
        ProgramPrioritas programPrioritas = laporanProgramPrioritasRepository
                .findById(programPrioritasAnggaran.idProgramPrioritas())
                .orElseThrow(() -> new ProgramPrioritasNotFoundException(programPrioritasAnggaran.idProgramPrioritas()));

        // 3. Ambil semua rencana kinerja untuk program prioritas anggaran ini
        Iterable<ProgramPrioritasAnggaranRencanaKinerja> rencanaKinerjaList =
                rencanaKinerjaRepository.findByIdProgramPrioritasAnggaran(programPrioritasAnggaran.id());

        List<RencanaKinerjaLaporanResponse> rencanaKinerjaResponses = new ArrayList<>();

        for (ProgramPrioritasAnggaranRencanaKinerja rkRelasi : rencanaKinerjaList) {
            if (isLevel2 && !allowedRencanaKinerjaIds.contains(rkRelasi.idRencanaKinerja())) {
                continue;
            }

            // 4. Ambil data RencanaKinerja
            RencanaKinerja rencanaKinerja = rencanaKinerjaEntityRepository
                    .findById(rkRelasi.idRencanaKinerja())
                    .orElse(null);

            if (rencanaKinerja != null) {
                // 5. Ambil data SubKegiatanRencanaKinerja
                List<SubKegiatanRencanaKinerja> subKegiatanList =
                        subKegiatanRencanaKinerjaRepository.findByIdRekin(
                                rencanaKinerja.id().intValue()
                        );

                // 6. Hitung total anggaran (pagu) dari SEMUA subkegiatan
                Integer pagu = 0;
                String kodeSubkegiatan = null;
                String namaSubkegiatan = null;

                if (!subKegiatanList.isEmpty()) {
                    // Ambil data subkegiatan pertama untuk informasi
                    SubKegiatanRencanaKinerja subKegiatanPertama = subKegiatanList.get(0);
                    kodeSubkegiatan = subKegiatanPertama.kodeSubKegiatan();
                    namaSubkegiatan = subKegiatanPertama.namaSubKegiatan();
                    
                    // Jumlahkan pagu dari SEMUA subkegiatan
                    for (SubKegiatanRencanaKinerja subKegiatan : subKegiatanList) {
                        List<RincianBelanja> rincianBelanjaList =
                                rincianBelanjaRepository.findByIdSubkegiatanRencanaKinerja(subKegiatan.id());
                        pagu += rincianBelanjaList.stream()
                                .mapToInt(RincianBelanja::totalAnggaran)
                                .sum();
                    }
                }

                // 7. Hitung tahapan pelaksanaan (TW)
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

                // 8. Buat RencanaKinerjaLaporanResponse
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

        if (isLevel2 && rencanaKinerjaResponses.isEmpty()) {
            return null;
        }

        // 9. Buat list pelaksana
        String kodeOpd = programPrioritasAnggaran.kodeOpd();
        String namaOpd = opdRepository.findByKodeOpd(kodeOpd)
                .map(Opd::namaOpd)
                .orElse(null);

        List<PelaksanaLaporanResponse> pelaksanas = new ArrayList<>();
        if (!rencanaKinerjaResponses.isEmpty()) {
            pelaksanas.add(new PelaksanaLaporanResponse(
                    getNamaPegawaiByNip(programPrioritasAnggaran.nip(), kodeOpd, programPrioritasAnggaran.tahun()),
                    programPrioritasAnggaran.nip(),
                    rencanaKinerjaResponses
            ));
        }

        // 10. Buat LaporanProgramPrioritasDataResponse
        // id_program_prioritas diambil dari tabel program_prioritas_anggaran (programPrioritasAnggaran.id())
        return new LaporanProgramPrioritasDataResponse(
                programPrioritasAnggaran.id(), // ← diambil dari program_prioritas_anggaran
                programPrioritas.programPrioritas(),
                tahun,
                kodeOpd,
                namaOpd,
                pelaksanas,
                "-"
        );
    }

    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritas(
            List<Long> idProgramPrioritasAnggaranList,
            Integer tahun,
            String requesterNip,
            boolean isLevel2
    ) {
        Set<Long> allowedRencanaKinerjaIds = isLevel2
                ? rencanaKinerjaVerifikatorRepository.findByNipVerifikator(requesterNip).stream()
                .map(relasi -> relasi.idRencanaKinerja())
                .collect(Collectors.toSet())
                : Set.of();

        return idProgramPrioritasAnggaranList.stream()
                .map(id -> getLaporan(id, tahun, isLevel2, allowedRencanaKinerjaIds))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritasByPegawai(
            String kodeOpd,
            Integer tahun,
            String nipPegawai
    ) {
        List<ProgramPrioritasAnggaran> anggaranList = StreamSupport.stream(
                        programPrioritasAnggaranRepository.findByKodeOpdAndTahun(kodeOpd, tahun).spliterator(),
                        false
                )
                .toList();

        if (anggaranList.isEmpty()) {
            return List.of();
        }

        return anggaranList.stream()
                .map(anggaran -> getLaporanForPegawai(anggaran, nipPegawai))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritasAllOpd(
            Integer tahun,
            String requesterNip,
            boolean isLevel2
    ) {
        List<Long> ids = StreamSupport.stream(
                        programPrioritasAnggaranRepository.findByTahun(tahun).spliterator(),
                        false
                )
                .map(ProgramPrioritasAnggaran::id)
                .toList();

        if (ids.isEmpty()) {
            return List.of();
        }

        return getLaporanProgramPrioritas(ids, tahun, requesterNip, isLevel2);
    }

    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritasByKodeOpd(
            String kodeOpd,
            Integer tahun,
            String requesterNip,
            boolean isLevel2
    ) {
        List<Long> ids = StreamSupport.stream(
                        programPrioritasAnggaranRepository.findByKodeOpdAndTahun(kodeOpd, tahun).spliterator(),
                        false
                )
                .map(ProgramPrioritasAnggaran::id)
                .toList();

        if (ids.isEmpty()) {
            return List.of();
        }

        return getLaporanProgramPrioritas(ids, tahun, requesterNip, isLevel2);
    }

    private LaporanProgramPrioritasDataResponse getLaporanForPegawai(
            ProgramPrioritasAnggaran programPrioritasAnggaran,
            String nipPegawai
    ) {
        ProgramPrioritas programPrioritas = laporanProgramPrioritasRepository
                .findById(programPrioritasAnggaran.idProgramPrioritas())
                .orElseThrow(() -> new ProgramPrioritasNotFoundException(programPrioritasAnggaran.idProgramPrioritas()));

        Iterable<ProgramPrioritasAnggaranRencanaKinerja> rencanaKinerjaList =
                rencanaKinerjaRepository.findByIdProgramPrioritasAnggaran(programPrioritasAnggaran.id());

        List<RencanaKinerjaLaporanResponse> rencanaKinerjaResponses = new ArrayList<>();

        for (ProgramPrioritasAnggaranRencanaKinerja rkRelasi : rencanaKinerjaList) {
            RencanaKinerja rencanaKinerja = rencanaKinerjaEntityRepository
                    .findById(rkRelasi.idRencanaKinerja())
                    .orElse(null);

            if (rencanaKinerja == null) {
                continue;
            }

            if (!Objects.equals(rencanaKinerja.nipPegawai(), nipPegawai)) {
                continue;
            }

            List<SubKegiatanRencanaKinerja> subKegiatanList =
                    subKegiatanRencanaKinerjaRepository.findByIdRekin(
                            rencanaKinerja.id().intValue()
                    );

            Integer pagu = 0;
            String kodeSubkegiatan = null;
            String namaSubkegiatan = null;

            if (!subKegiatanList.isEmpty()) {
                SubKegiatanRencanaKinerja subKegiatanPertama = subKegiatanList.get(0);
                kodeSubkegiatan = subKegiatanPertama.kodeSubKegiatan();
                namaSubkegiatan = subKegiatanPertama.namaSubKegiatan();

                for (SubKegiatanRencanaKinerja subKegiatan : subKegiatanList) {
                    List<RincianBelanja> rincianBelanjaList =
                            rincianBelanjaRepository.findByIdSubkegiatanRencanaKinerja(subKegiatan.id());
                    pagu += rincianBelanjaList.stream()
                            .mapToInt(RincianBelanja::totalAnggaran)
                            .sum();
                }
            }

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

        if (rencanaKinerjaResponses.isEmpty()) {
            return null;
        }

        String kodeOpd = programPrioritasAnggaran.kodeOpd();
        String namaOpd = opdRepository.findByKodeOpd(kodeOpd)
                .map(Opd::namaOpd)
                .orElse(null);

        List<PelaksanaLaporanResponse> pelaksanas = new ArrayList<>();
        pelaksanas.add(new PelaksanaLaporanResponse(
                getNamaPegawaiByNip(nipPegawai, kodeOpd, programPrioritasAnggaran.tahun()),
                nipPegawai,
                rencanaKinerjaResponses
        ));

        return new LaporanProgramPrioritasDataResponse(
                programPrioritasAnggaran.id(),
                programPrioritas.programPrioritas(),
                programPrioritasAnggaran.tahun(),
                kodeOpd,
                namaOpd,
                pelaksanas,
                "-"
        );
    }

    public List<LaporanProgramPrioritasDataResponse> getLaporanProgramPrioritasVerified(
            List<Long> idProgramPrioritasAnggaranList,
            String requesterNip,
            boolean isLevel2,
            String filterHash
    ) {
        // filterHash tetap diterima untuk kompatibilitas, tetapi tidak digunakan.
        if (idProgramPrioritasAnggaranList == null || idProgramPrioritasAnggaranList.isEmpty()) {
            return List.of();
        }

        List<ProgramPrioritasAnggaran> programPrioritasAnggarans = idProgramPrioritasAnggaranList.stream()
                .map(id -> programPrioritasAnggaranRepository.findById(id)
                        .orElseThrow(() -> new ProgramPrioritasAnggaranNotFoundException(id)))
                .toList();

        String kodeOpd = programPrioritasAnggarans.stream()
                .map(ProgramPrioritasAnggaran::kodeOpd)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        boolean hasKodeOpd = kodeOpd != null && !kodeOpd.isBlank();
        boolean mixedKodeOpd = programPrioritasAnggarans.stream()
                .map(ProgramPrioritasAnggaran::kodeOpd)
                .filter(Objects::nonNull)
                .anyMatch(existing -> !Objects.equals(existing, kodeOpd));
        if (!hasKodeOpd || mixedKodeOpd) {
            return List.of();
        }

        Set<Long> allowedRencanaKinerjaIds = isLevel2
                ? rencanaKinerjaVerifikatorRepository.findByNipVerifikator(requesterNip).stream()
                .map(relasi -> relasi.idRencanaKinerja())
                .collect(Collectors.toSet())
                : Set.of();

        return programPrioritasAnggarans.stream()
                .map(anggaran -> getLaporanVerified(anggaran, isLevel2, allowedRencanaKinerjaIds))
                .filter(Objects::nonNull)
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

    private LaporanProgramPrioritasDataResponse getLaporanVerified(
            ProgramPrioritasAnggaran programPrioritasAnggaran,
            boolean isLevel2,
            Set<Long> allowedRencanaKinerjaIds
    ) {
        ProgramPrioritas programPrioritas = laporanProgramPrioritasRepository
                .findById(programPrioritasAnggaran.idProgramPrioritas())
                .orElseThrow(() -> new ProgramPrioritasNotFoundException(programPrioritasAnggaran.idProgramPrioritas()));

        Iterable<ProgramPrioritasAnggaranRencanaKinerja> rencanaKinerjaList =
                rencanaKinerjaRepository.findByIdProgramPrioritasAnggaran(programPrioritasAnggaran.id());

        List<RencanaKinerjaLaporanResponse> rencanaKinerjaResponses = new ArrayList<>();

        for (ProgramPrioritasAnggaranRencanaKinerja rkRelasi : rencanaKinerjaList) {
            if (isLevel2 && !allowedRencanaKinerjaIds.contains(rkRelasi.idRencanaKinerja())) {
                continue;
            }

            if (rencanaKinerjaVerifikatorRepository.findByIdRencanaKinerja(rkRelasi.idRencanaKinerja()).isEmpty()) {
                continue;
            }

            RencanaKinerja rencanaKinerja = rencanaKinerjaEntityRepository
                    .findById(rkRelasi.idRencanaKinerja())
                    .orElse(null);

            if (rencanaKinerja != null) {
                List<SubKegiatanRencanaKinerja> subKegiatanList =
                        subKegiatanRencanaKinerjaRepository.findByIdRekin(
                                rencanaKinerja.id().intValue()
                        );

                Integer pagu = 0;
                String kodeSubkegiatan = null;
                String namaSubkegiatan = null;

                if (!subKegiatanList.isEmpty()) {
                    SubKegiatanRencanaKinerja subKegiatanPertama = subKegiatanList.get(0);
                    kodeSubkegiatan = subKegiatanPertama.kodeSubKegiatan();
                    namaSubkegiatan = subKegiatanPertama.namaSubKegiatan();

                    for (SubKegiatanRencanaKinerja subKegiatan : subKegiatanList) {
                        List<RincianBelanja> rincianBelanjaList =
                                rincianBelanjaRepository.findByIdSubkegiatanRencanaKinerja(subKegiatan.id());
                        pagu += rincianBelanjaList.stream()
                                .mapToInt(RincianBelanja::totalAnggaran)
                                .sum();
                    }
                }

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

        if (isLevel2 && rencanaKinerjaResponses.isEmpty()) {
            return null;
        }

        String kodeOpd = programPrioritasAnggaran.kodeOpd();
        String namaOpd = opdRepository.findByKodeOpd(kodeOpd)
                .map(Opd::namaOpd)
                .orElse(null);

        List<PelaksanaLaporanResponse> pelaksanas = new ArrayList<>();
          if (!rencanaKinerjaResponses.isEmpty()) {
              pelaksanas.add(new PelaksanaLaporanResponse(
                      getNamaPegawaiByNip(programPrioritasAnggaran.nip(), kodeOpd, programPrioritasAnggaran.tahun()),
                      programPrioritasAnggaran.nip(),
                      rencanaKinerjaResponses
              ));
          }

        return new LaporanProgramPrioritasDataResponse(
                programPrioritasAnggaran.id(),
                programPrioritas.programPrioritas(),
                programPrioritasAnggaran.tahun(),
                kodeOpd,
                namaOpd,
                pelaksanas,
                "-"
        );
    }
}
