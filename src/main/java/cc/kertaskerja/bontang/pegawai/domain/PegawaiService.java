package cc.kertaskerja.bontang.pegawai.domain;

import cc.kertaskerja.bontang.pegawai.domain.exception.PegawaiNotFoundException;
import cc.kertaskerja.bontang.pegawai.web.PegawaiRequest;
import org.springframework.stereotype.Service;

@Service
public class PegawaiService {
    public final PegawaiRepository pegawaiRepository;

    public PegawaiService(PegawaiRepository pegawaiRepository) {
        this.pegawaiRepository = pegawaiRepository;
    }

    public Iterable<Pegawai> findByKodeOpdAndTahun(String kodeOpd, Integer tahun) {
        return pegawaiRepository.findByKodeOpdAndTahun(kodeOpd, tahun);
    }

    public Pegawai detailPegawaiByNip(String nip) {
        return pegawaiRepository.findByNip(nip)
                .orElseThrow(() -> new PegawaiNotFoundException(nip));
    }

    public Pegawai tambahPegawai(PegawaiRequest request) {
        Pegawai pegawai = Pegawai.of(
                request.kodeOpd(),
                request.tahun(),
                request.namaPegawai(),
                request.nip(),
                request.email(),
                request.jabatanDinas(),
                request.jabatanTim(),
                request.role()
        );
        return pegawaiRepository.save(pegawai);
    }

    public Pegawai ubahPegawai(String nip, PegawaiRequest request) {
        Pegawai existingPegawai = detailPegawaiByNip(nip);

        Pegawai pegawai = new Pegawai(
                existingPegawai.id(),
                request.kodeOpd(),
                request.tahun(),
                request.namaPegawai(),
                request.nip(),
                request.email(),
                request.jabatanDinas(),
                request.jabatanTim(),
                request.role(),
                existingPegawai.createdDate(),
                null
        );

        return pegawaiRepository.save(pegawai);
    }

    public void hapusPegawai(String nip) {
        if (!pegawaiRepository.existsByNip(nip)) {
            throw new PegawaiNotFoundException(nip);
        }

        pegawaiRepository.deleteByNip(nip);
    }
}
