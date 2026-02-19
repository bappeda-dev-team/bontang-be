package cc.kertaskerja.bontang.pegawai.domain;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cc.kertaskerja.bontang.auth.domain.PegawaiAuthRepository;
import cc.kertaskerja.bontang.auth.domain.RoleNormalizer;
import cc.kertaskerja.bontang.pegawai.domain.exception.PegawaiAlreadyExistException;
import cc.kertaskerja.bontang.pegawai.domain.exception.PegawaiNotFoundException;
import cc.kertaskerja.bontang.pegawai.web.PegawaiRequest;

@Service
public class PegawaiService {
    private final PegawaiRepository pegawaiRepository;
    private final PegawaiAuthRepository pegawaiAuthRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleNormalizer roleNormalizer;

    public PegawaiService(
            PegawaiRepository pegawaiRepository,
            PegawaiAuthRepository pegawaiAuthRepository,
            PasswordEncoder passwordEncoder,
            RoleNormalizer roleNormalizer
    ) {
        this.pegawaiRepository = pegawaiRepository;
        this.pegawaiAuthRepository = pegawaiAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleNormalizer = roleNormalizer;
    }

    public Iterable<Pegawai> findByKodeOpdAndTahun(String kodeOpd, Integer tahun) {
        return pegawaiRepository.findByKodeOpdAndTahun(kodeOpd, tahun);
    }

    public Pegawai detailPegawaiByNip(String nip) {
        return pegawaiRepository.findByNip(nip)
                .orElseThrow(() -> new PegawaiNotFoundException(nip));
    }

    public Pegawai tambahPegawai(PegawaiRequest request) {
        if (pegawaiRepository.existsByNip(request.nip())) {
            throw new PegawaiAlreadyExistException(request.nip());
        }

        if (request.password() == null || request.password().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password tidak boleh kosong");
        }

        String normalizedRole = roleNormalizer.normalize(request.role());

        Pegawai pegawai = Pegawai.of(
                request.kodeOpd(),
                request.tahun(),
                request.namaPegawai(),
                request.nip(),
                request.email(),
                normalizedRole,
                request.jabatanId()
        );

        Pegawai saved = pegawaiRepository.save(pegawai);
        String passwordHash = passwordEncoder.encode(request.password());
        int rows = pegawaiAuthRepository.setPasswordHashAndRole(saved.nip(), passwordHash, normalizedRole);
        if (rows <= 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Gagal menyimpan kredensial pegawai");
        }

        return saved;
    }

    public Pegawai ubahPegawai(String nip, PegawaiRequest request) {
        Pegawai existingPegawai = detailPegawaiByNip(nip);

        String normalizedRole = request.role() == null
                ? existingPegawai.role()
                : roleNormalizer.normalize(request.role());

        Pegawai pegawai = new Pegawai(
                existingPegawai.id(),
                request.kodeOpd(),
                request.tahun(),
                request.namaPegawai(),
                request.nip(),
                request.email(),
                normalizedRole,
                request.jabatanId(),
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
