package cc.kertaskerja.bontang.program.domain;

import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusan;
import cc.kertaskerja.bontang.bidangurusan.domain.BidangUrusanRepository;
import cc.kertaskerja.bontang.bidangurusan.domain.exception.BidangUrusanNotFoundException;
import org.springframework.stereotype.Service;

import cc.kertaskerja.bontang.program.domain.exception.ProgramNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final BidangUrusanRepository bidangUrusanRepository;

    public ProgramService(
            ProgramRepository programRepository,
            BidangUrusanRepository bidangUrusanRepository
    ) {
        this.programRepository = programRepository;
        this.bidangUrusanRepository = bidangUrusanRepository;
    }

    public Iterable<Program> findAll() {
        return programRepository.findAll();
    }

    public String getKodeBidangUrusan(Long bidangUrusanId) {
        return bidangUrusanRepository.findById(bidangUrusanId)
                .map(BidangUrusan::kodeBidangUrusan)
                .orElseThrow(() -> new BidangUrusanNotFoundException(String.valueOf(bidangUrusanId)));
    }

    public Program detailProgramByKodeProgram(String kodeProgram) {
        return programRepository.findByKodeProgram(kodeProgram)
                .orElseThrow(() -> new ProgramNotFoundException(kodeProgram));
    }

    public Iterable<Program> findByKodeOpd(String kodeOpd) {
        return programRepository.findByKodeOpd(kodeOpd);
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

    public Program tambahProgram(Program program) {

        return programRepository.save(program);
    }

    public Program ubahProgram(String kodeProgram, Program program) {
        if (!programRepository.existsByKodeProgram(kodeProgram)) {
            throw new ProgramNotFoundException(kodeProgram);
        }

        return programRepository.save(program);
    }

    public void hapusProgram(String kodeProgram) {
        if (!programRepository.existsByKodeProgram(kodeProgram)) {
            throw new ProgramNotFoundException(kodeProgram);
        }

        programRepository.deleteByKodeProgram(kodeProgram);
    }
}
