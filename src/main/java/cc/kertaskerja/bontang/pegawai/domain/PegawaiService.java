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

    public Iterable<Pegawai> findAll() {
        return pegawaiRepository.findAll();
    }

    public Pegawai detailPegawaiByNip(String nip) {
        return pegawaiRepository.findByNip(nip)
                .orElseThrow(() -> new PegawaiNotFoundException(nip));
    }

    public Pegawai tambahPegawai(PegawaiRequest request) {
        Pegawai pegawai = Pegawai.of(
                null,
                null,
                request.namaPegawai(),
                request.nip(),
                request.email(),
                request.jabatanDinas(),
                request.jabatanTim()
        );
        return pegawaiRepository.save(pegawai);
    }

    public Pegawai ubahPegawai(String nip, PegawaiRequest request) {
        Pegawai existingPegawai = detailPegawaiByNip(nip);

        Pegawai pegawai = new Pegawai(
                existingPegawai.id(),
                existingPegawai.kodeOpd(),
                existingPegawai.tahun(),
                request.namaPegawai(),
                request.nip(),
                request.email(),
                request.jabatanDinas(),
                request.jabatanTim(),
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
