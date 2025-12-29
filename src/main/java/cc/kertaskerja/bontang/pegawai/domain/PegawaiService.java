package cc.kertaskerja.bontang.pegawai.domain;

import cc.kertaskerja.bontang.pegawai.domain.exception.PegawaiNotFoundException;
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

    public Pegawai tambahPegawai(Pegawai pegawai) {
        return pegawaiRepository.save(pegawai);
    }

    public Pegawai ubahPegawai(String nip, Pegawai pegawai) {
        if (!pegawaiRepository.existsByNip(nip)) {
            throw new PegawaiNotFoundException(nip);
        }

        return pegawaiRepository.save(pegawai);
    }

    public void hapusPegawai(String nip) {
        if (!pegawaiRepository.existsByNip(nip)) {
            throw new PegawaiNotFoundException(nip);
        }

        pegawaiRepository.deleteByNip(nip);
    }
}
