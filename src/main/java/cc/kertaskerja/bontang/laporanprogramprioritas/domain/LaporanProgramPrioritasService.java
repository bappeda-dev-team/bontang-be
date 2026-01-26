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
import cc.kertaskerja.bontang.programprioritasanggaran.domain.exception.ProgramPrioritasAnggaranNotFoundException;
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
import java.util.List;

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
            Long idProgramPrioritasAnggaran,
            String kodeOpd,
            String namaOpd,
            Integer tahun
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

                // 6. Hitung total anggaran (pagu)
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
                            .findFirst()
                            .map(RincianBelanja::totalAnggaran)
                            .orElse(0);
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

        // 9. Buat list pelaksana
        List<PelaksanaLaporanResponse> pelaksanas = new ArrayList<>();
        if (!rencanaKinerjaResponses.isEmpty()) {
            pelaksanas.add(new PelaksanaLaporanResponse(
                    getNamaPegawaiByNip(programPrioritasAnggaran.nip(), kodeOpd, tahun),
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
            String kodeOpd,
            String namaOpd,
            Integer tahun
    ) {
        return idProgramPrioritasAnggaranList.stream()
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
