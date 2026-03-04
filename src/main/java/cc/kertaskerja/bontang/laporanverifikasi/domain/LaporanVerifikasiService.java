package cc.kertaskerja.bontang.laporanverifikasi.domain;

import cc.kertaskerja.bontang.jabatan.domain.Jabatan;
import cc.kertaskerja.bontang.jabatan.domain.JabatanRepository;
import cc.kertaskerja.bontang.laporanprogramprioritas.domain.LaporanProgramPrioritasService;
import cc.kertaskerja.bontang.laporanprogramprioritas.web.response.LaporanProgramPrioritasDataResponse;
import cc.kertaskerja.bontang.laporanrincianbelanja.domain.LaporanRincianBelanjaService;
import cc.kertaskerja.bontang.laporanrincianbelanja.web.response.LaporanRincianBelanjaEnvelopeResponse;
import cc.kertaskerja.bontang.laporanverifikasi.web.LaporanVerifikasiRequest;
import cc.kertaskerja.bontang.laporanverifikasi.web.response.LaporanCetakResponse;
import cc.kertaskerja.bontang.laporanverifikasi.web.response.LaporanPenandatanganResponse;
import cc.kertaskerja.bontang.laporanverifikasi.web.response.LaporanVerifikasiResultResponse;
import cc.kertaskerja.bontang.laporanverifikasi.web.response.LaporanVerifikasiStatusResponse;
import cc.kertaskerja.bontang.pegawai.domain.Pegawai;
import cc.kertaskerja.bontang.pegawai.domain.PegawaiRepository;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaran;
import cc.kertaskerja.bontang.programprioritasanggaran.domain.ProgramPrioritasAnggaranRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerja;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaRepository;
import cc.kertaskerja.bontang.rencanakinerja.domain.RencanaKinerjaVerifikatorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LaporanVerifikasiService {
    private final LaporanVerifikasiRepository laporanVerifikasiRepository;
    private final RencanaKinerjaVerifikatorRepository rencanaKinerjaVerifikatorRepository;
    private final RencanaKinerjaRepository rencanaKinerjaRepository;
    private final PegawaiRepository pegawaiRepository;
    private final JabatanRepository jabatanRepository;
    private final LaporanProgramPrioritasService laporanProgramPrioritasService;
    private final LaporanRincianBelanjaService laporanRincianBelanjaService;
    private final ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository;

    public LaporanVerifikasiService(
            LaporanVerifikasiRepository laporanVerifikasiRepository,
            RencanaKinerjaVerifikatorRepository rencanaKinerjaVerifikatorRepository,
            RencanaKinerjaRepository rencanaKinerjaRepository,
            PegawaiRepository pegawaiRepository,
            JabatanRepository jabatanRepository,
            LaporanProgramPrioritasService laporanProgramPrioritasService,
            LaporanRincianBelanjaService laporanRincianBelanjaService,
            ProgramPrioritasAnggaranRepository programPrioritasAnggaranRepository
    ) {
        this.laporanVerifikasiRepository = laporanVerifikasiRepository;
        this.rencanaKinerjaVerifikatorRepository = rencanaKinerjaVerifikatorRepository;
        this.rencanaKinerjaRepository = rencanaKinerjaRepository;
        this.pegawaiRepository = pegawaiRepository;
        this.jabatanRepository = jabatanRepository;
        this.laporanProgramPrioritasService = laporanProgramPrioritasService;
        this.laporanRincianBelanjaService = laporanRincianBelanjaService;
        this.programPrioritasAnggaranRepository = programPrioritasAnggaranRepository;
    }

    public LaporanVerifikasiResultResponse verifikasiLaporan(
            LaporanVerifikasiRequest request,
            Authentication authentication
    ) {
        boolean isLevel1 = hasRole(authentication, "ROLE_LEVEL_1");
        assertLevel1OrLevel2(authentication);
        String requesterNip = authentication.getName();
        LaporanJenis jenis = LaporanJenis.fromRaw(request.jenisLaporan());
        String filterHash = normalizeFilterHash(request.filterHash());

        if (!isLevel1) {
            ensureScopeLevel2(requesterNip, request.kodeOpd(), request.tahun());
        }

        Instant now = Instant.now();
        LaporanVerifikasi existing = laporanVerifikasiRepository
                .findByJenisLaporanAndKodeOpdAndTahunAndFilterHash(
                        jenis.name(),
                        request.kodeOpd(),
                        request.tahun(),
                        filterHash
                )
                .orElse(null);

        LaporanVerifikasi toSave = existing == null
                ? LaporanVerifikasi.of(jenis.name(), request.kodeOpd(), request.tahun(), filterHash, requesterNip, now)
                : new LaporanVerifikasi(
                        existing.id(),
                        existing.jenisLaporan(),
                        existing.kodeOpd(),
                        existing.tahun(),
                        existing.filterHash(),
                        requesterNip,
                        now,
                        existing.createdDate(),
                        existing.lastModifiedDate()
                );

        LaporanVerifikasi saved = laporanVerifikasiRepository.save(toSave);
        String verifiedByNama = pegawaiRepository.findByNip(saved.verifiedByNip())
                .map(Pegawai::namaPegawai)
                .orElse("-");

        return new LaporanVerifikasiResultResponse(
                saved.jenisLaporan(),
                saved.kodeOpd(),
                saved.tahun(),
                saved.filterHash(),
                saved.verifiedByNip(),
                verifiedByNama,
                saved.verifiedAt()
        );
    }

    public LaporanVerifikasiStatusResponse getStatus(
            String jenisLaporan,
            String kodeOpd,
            Integer tahun,
            String filterHash
    ) {
        LaporanJenis jenis = LaporanJenis.fromRaw(jenisLaporan);
        String normalizedFilterHash = normalizeFilterHash(filterHash);

        LaporanVerifikasi data = laporanVerifikasiRepository
                .findByJenisLaporanAndKodeOpdAndTahunAndFilterHash(
                        jenis.name(),
                        kodeOpd,
                        tahun,
                        normalizedFilterHash
                )
                .orElse(null);

        if (data == null) {
            return new LaporanVerifikasiStatusResponse(
                    false,
                    jenis.name(),
                    kodeOpd,
                    tahun,
                    normalizedFilterHash,
                    null,
                    null,
                    null
            );
        }

        String verifiedByNama = pegawaiRepository.findByNip(data.verifiedByNip())
                .map(Pegawai::namaPegawai)
                .orElse("-");

        return new LaporanVerifikasiStatusResponse(
                true,
                data.jenisLaporan(),
                data.kodeOpd(),
                data.tahun(),
                data.filterHash(),
                data.verifiedByNip(),
                verifiedByNama,
                data.verifiedAt()
        );
    }

    public LaporanCetakResponse getCetak(
            String jenisLaporan,
            String kodeOpd,
            Integer tahun,
            String filterHash,
            Authentication authentication
    ) {
        LaporanJenis jenis = LaporanJenis.fromRaw(jenisLaporan);
        String normalizedFilterHash = normalizeFilterHash(filterHash);
        String requesterNip = authentication.getName();
        boolean isLevel2 = hasRole(authentication, "ROLE_LEVEL_2");

        LaporanVerifikasi verifikasi = laporanVerifikasiRepository
                .findByJenisLaporanAndKodeOpdAndTahunAndFilterHash(
                        jenis.name(),
                        kodeOpd,
                        tahun,
                        normalizedFilterHash
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Laporan belum diverifikasi untuk filter ini"
                ));

        Object data = switch (jenis) {
            case PROGRAM_PRIORITAS -> {
                List<Long> ids = StreamSupport.stream(
                                programPrioritasAnggaranRepository.findByKodeOpdAndTahun(kodeOpd, tahun).spliterator(),
                                false
                        )
                        .map(ProgramPrioritasAnggaran::id)
                        .toList();
                yield laporanProgramPrioritasService.getLaporanProgramPrioritas(ids, tahun, requesterNip, isLevel2);
            }
            case RINCIAN_BELANJA -> laporanRincianBelanjaService.getLaporanRincianBelanja(
                    kodeOpd,
                    tahun,
                    requesterNip,
                    isLevel2
            );
        };

        String verifiedByNama = pegawaiRepository.findByNip(verifikasi.verifiedByNip())
                .map(Pegawai::namaPegawai)
                .orElse("-");
        LaporanPenandatanganResponse penandatangan = buildPenandatangan(verifikasi.verifiedByNip());

        return new LaporanCetakResponse(
                verifikasi.jenisLaporan(),
                verifikasi.kodeOpd(),
                verifikasi.tahun(),
                verifikasi.filterHash(),
                verifikasi.verifiedByNip(),
                verifiedByNama,
                verifikasi.verifiedAt(),
                penandatangan,
                data
        );
    }

    public List<LaporanProgramPrioritasDataResponse> getVerifiedProgramPrioritas(
            String kodeOpd,
            Integer tahun,
            String filterHash,
            Authentication authentication
    ) {
        String normalizedFilterHash = normalizeFilterHash(filterHash);
        boolean verified = laporanVerifikasiRepository
                .findByJenisLaporanAndKodeOpdAndTahunAndFilterHash(
                        LaporanJenis.PROGRAM_PRIORITAS.name(),
                        kodeOpd,
                        tahun,
                        normalizedFilterHash
                )
                .isPresent();

        if (!verified) {
            return List.of();
        }

        List<Long> ids = StreamSupport.stream(
                        programPrioritasAnggaranRepository.findByKodeOpdAndTahun(kodeOpd, tahun).spliterator(),
                        false
                )
                .map(ProgramPrioritasAnggaran::id)
                .toList();

        if (ids.isEmpty()) {
            return List.of();
        }

        return laporanProgramPrioritasService.getLaporanProgramPrioritas(
                ids,
                tahun,
                authentication.getName(),
                false
        );
    }

    public LaporanRincianBelanjaEnvelopeResponse getVerifiedRincianBelanja(
            String kodeOpd,
            Integer tahun,
            String filterHash,
            Authentication authentication
    ) {
        String normalizedFilterHash = normalizeFilterHash(filterHash);
        boolean verified = laporanVerifikasiRepository
                .findByJenisLaporanAndKodeOpdAndTahunAndFilterHash(
                        LaporanJenis.RINCIAN_BELANJA.name(),
                        kodeOpd,
                        tahun,
                        normalizedFilterHash
                )
                .isPresent();

        if (!verified) {
            return new LaporanRincianBelanjaEnvelopeResponse(
                    200,
                    "success get laporan rincian belanja",
                    List.of()
            );
        }

        return laporanRincianBelanjaService.getLaporanRincianBelanja(
                kodeOpd,
                tahun,
                authentication.getName(),
                false
        );
    }

    private LaporanPenandatanganResponse buildPenandatangan(String nip) {
        Pegawai pegawai = pegawaiRepository.findByNip(nip).orElse(null);
        if (pegawai == null) {
            return new LaporanPenandatanganResponse("-", nip, "-");
        }

        String namaJabatan = "-";
        if (pegawai.jabatanId() != null) {
            namaJabatan = jabatanRepository.findById(pegawai.jabatanId())
                    .map(Jabatan::namaJabatan)
                    .orElse("-");
        }

        return new LaporanPenandatanganResponse(
                pegawai.namaPegawai() != null ? pegawai.namaPegawai() : "-",
                pegawai.nip(),
                namaJabatan
        );
    }

    private void ensureScopeLevel2(String requesterNip, String kodeOpd, Integer tahun) {
        Set<Long> allowedRencanaKinerjaIds = rencanaKinerjaVerifikatorRepository.findByNipVerifikator(requesterNip)
                .stream()
                .map(relasi -> relasi.idRencanaKinerja())
                .collect(Collectors.toSet());

        if (allowedRencanaKinerjaIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Anda tidak memiliki scope verifikasi");
        }

        Set<Long> scopeIds = new HashSet<>();
        for (RencanaKinerja rekin : rencanaKinerjaRepository.findByKodeOpdAndTahun(kodeOpd, tahun)) {
            scopeIds.add(rekin.id());
        }

        boolean hasIntersection = scopeIds.stream().anyMatch(allowedRencanaKinerjaIds::contains);
        if (!hasIntersection) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Data laporan di luar scope verifikasi anda");
        }
    }

    private void assertLevel1OrLevel2(Authentication authentication) {
        boolean isLevel1 = hasRole(authentication, "ROLE_LEVEL_1");
        boolean isLevel2 = hasRole(authentication, "ROLE_LEVEL_2");

        if (!isLevel1 && !isLevel2) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Hanya LEVEL_1 atau LEVEL_2 yang dapat memverifikasi laporan");
        }
    }

    private boolean hasRole(Authentication authentication, String roleName) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(roleName, authority.getAuthority()));
    }

    private String normalizeFilterHash(String filterHash) {
        if (filterHash == null || filterHash.isBlank()) {
            return "-";
        }
        return filterHash.trim();
    }
}
