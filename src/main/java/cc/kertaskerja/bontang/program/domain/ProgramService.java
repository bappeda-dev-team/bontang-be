package cc.kertaskerja.bontang.program.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanRepository;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import cc.kertaskerja.bontang.kegiatan.domain.KegiatanRepository;
import cc.kertaskerja.bontang.program.domain.exception.ProgramDeleteForbiddenException;
import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final BidangUrusanRepository bidangUrusanRepository;
    private final KegiatanRepository kegiatanRepository;

    public ProgramService(
            ProgramRepository programRepository,
            BidangUrusanRepository bidangUrusanRepository,
            KegiatanRepository kegiatanRepository
    ) {
        this.programRepository = programRepository;
        this.bidangUrusanRepository = bidangUrusanRepository;
        this.kegiatanRepository = kegiatanRepository;
    }

    public Iterable<Program> findAll() {
        return programRepository.findAll();
    }

    public String getKodeBidangUrusan(Long bidangUrusanId) {
        return bidangUrusanRepository.findById(bidangUrusanId)
                .map(BidangUrusan::kodeBidangUrusan)
                .orElseThrow(() -> new BidangUrusanNotFoundException(String.valueOf(bidangUrusanId)));
    }

    private Long getBidangUrusanId(String kodeBidangUrusan) {
        return bidangUrusanRepository.findByKodeBidangUrusan(kodeBidangUrusan)
                .map(BidangUrusan::id)
                .orElseThrow(() -> new BidangUrusanNotFoundException(kodeBidangUrusan));
    }

    private Program attachBidangUrusan(Program program, Long bidangUrusanId) {
        return new Program(
                program.id(),
                program.kodeProgram(),
                program.namaProgram(),
                bidangUrusanId,
                program.createdDate(),
                program.lastModifiedDate()
        );
    }

    public Program detailProgramByKodeProgram(String kodeProgram) {
        return programRepository.findByKodeProgram(kodeProgram)
                .orElseThrow(() -> new ProgramNotFoundException(kodeProgram));
    }

    public List<Program> detailProgramByKodeProgramIn(List<String> kodePrograms) {
        List<Program> programs = programRepository.findAllByKodeProgramIn(kodePrograms);
        if (programs.size() != kodePrograms.size()) {
            Set<String> foundKode = programs.stream()
                    .map(Program::kodeProgram)
                    .collect(Collectors.toSet());

            String missingKode = kodePrograms.stream()
                    .filter(kode -> !foundKode.contains(kode))
                    .findFirst()
                    .orElse(null);

            if (missingKode != null) {
                throw new ProgramNotFoundException(missingKode);
            }
        }

        return programs;
    }

    public Program tambahProgram(Program program, String kodeBidangUrusan) {
        Long bidangUrusanId = getBidangUrusanId(kodeBidangUrusan);
        Program programWithBidang = attachBidangUrusan(program, bidangUrusanId);

        return programRepository.save(programWithBidang);
    }

    public Program ubahProgram(String kodeProgram, Program program, String kodeBidangUrusan) {
        if (!programRepository.existsByKodeProgram(kodeProgram)) {
            throw new ProgramNotFoundException(kodeProgram);
        }

        Long bidangUrusanId = getBidangUrusanId(kodeBidangUrusan);
        Program programWithBidang = attachBidangUrusan(program, bidangUrusanId);

        return programRepository.save(programWithBidang);
    }

    public void hapusProgram(String kodeProgram) {
        Program program = programRepository.findByKodeProgram(kodeProgram)
                .orElseThrow(() -> new ProgramNotFoundException(kodeProgram));

        if (kegiatanRepository.existsByProgramId(program.id())) {
            throw new ProgramDeleteForbiddenException(kodeProgram);
        }

        programRepository.deleteByKodeProgram(kodeProgram);
    }
}
