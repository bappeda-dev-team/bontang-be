package cc.kertaskerja.bontang.jabatan.domain;

import cc.kertaskerja.bontang.jabatan.domain.exception.JabatanAlreadyExistException;
import cc.kertaskerja.bontang.jabatan.domain.exception.JabatanNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JabatanService {
    private final JabatanRepository jabatanRepository;

    public JabatanService(JabatanRepository jabatanRepository) {
        this.jabatanRepository = jabatanRepository;
    }

    public Iterable<Jabatan> findByKodeOpd(String kodeOpd) {
        return jabatanRepository.findByKodeOpd(kodeOpd);
    }

    public Jabatan detailJabatanByKodeJabatan(String kodeJabatan) {
        return jabatanRepository.findByKodeJabatan(kodeJabatan)
                .orElseThrow(() -> new JabatanNotFoundException(kodeJabatan));
    }

    public Jabatan tambahJabatan(Jabatan jabatan) {
        if (jabatanRepository.existsByKodeJabatan(jabatan.kodeJabatan())) {
            throw new JabatanAlreadyExistException(jabatan.kodeJabatan());
        }

        return jabatanRepository.save(jabatan);
    }

    public Jabatan ubahJabatan(String kodeJabatan, Jabatan jabatan) {
        Jabatan existing = detailJabatanByKodeJabatan(kodeJabatan);

        if (!kodeJabatan.equals(jabatan.kodeJabatan()) && jabatanRepository.existsByKodeJabatan(jabatan.kodeJabatan())) {
            throw new JabatanAlreadyExistException(jabatan.kodeJabatan());
        }

        Jabatan updated = new Jabatan(
                existing.id(),
                jabatan.namaJabatan(),
                jabatan.kodeJabatan(),
                jabatan.jenisJabatan(),
                jabatan.kodeOpd(),
                existing.createdDate(),
                null
        );

        return jabatanRepository.save(updated);
    }

    public void hapusJabatan(String kodeJabatan) {
        if (!jabatanRepository.existsByKodeJabatan(kodeJabatan)) {
            throw new JabatanNotFoundException(kodeJabatan);
        }

        jabatanRepository.deleteByKodeJabatan(kodeJabatan);
    }
}
