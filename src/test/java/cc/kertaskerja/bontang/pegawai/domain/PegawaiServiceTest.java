package cc.kertaskerja.bontang.pegawai.domain;

import cc.kertaskerja.bontang.auth.domain.PegawaiAuthRepository;
import cc.kertaskerja.bontang.auth.domain.RoleNormalizer;
import cc.kertaskerja.bontang.pegawai.web.PegawaiRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PegawaiServiceTest {
    @Mock
    private PegawaiRepository pegawaiRepository;

    @Mock
    private PegawaiAuthRepository pegawaiAuthRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private PegawaiService pegawaiService;

    @BeforeEach
    void setUp() {
        pegawaiService = new PegawaiService(
                pegawaiRepository,
                pegawaiAuthRepository,
                passwordEncoder,
                new RoleNormalizer()
        );
    }

    @Test
    void ubahPegawai_updatesPasswordWhenProvided() {
        String nip = "123456";
        Pegawai existing = new Pegawai(
                1L,
                "OPD-01",
                2025,
                "Awal",
                nip,
                "awal@contoh",
                "LEVEL_2",
                10L,
                Instant.EPOCH,
                Instant.EPOCH
        );
        PegawaiRequest request = new PegawaiRequest(
                "OPD-01",
                2025,
                "Baru",
                nip,
                "baru@contoh",
                "1",
                11L,
                "password-baru"
        );
        Pegawai saved = new Pegawai(
                1L,
                "OPD-01",
                2025,
                "Baru",
                nip,
                "baru@contoh",
                "LEVEL_1",
                11L,
                Instant.EPOCH,
                Instant.EPOCH
        );

        when(pegawaiRepository.findByNip(nip)).thenReturn(Optional.of(existing));
        when(pegawaiRepository.save(any())).thenReturn(saved);
        when(passwordEncoder.encode("password-baru")).thenReturn("hash");
        when(pegawaiAuthRepository.setPasswordHashAndRole(nip, "hash", "LEVEL_1")).thenReturn(1);

        Pegawai result = pegawaiService.ubahPegawai(nip, request);

        assertEquals(saved, result);
        verify(passwordEncoder).encode("password-baru");
        verify(pegawaiAuthRepository).setPasswordHashAndRole(nip, "hash", "LEVEL_1");
    }

    @Test
    void ubahPegawai_skipsPasswordWhenBlank() {
        String nip = "123456";
        Pegawai existing = new Pegawai(
                1L,
                "OPD-01",
                2025,
                "Awal",
                nip,
                "awal@contoh",
                "LEVEL_3",
                10L,
                Instant.EPOCH,
                Instant.EPOCH
        );
        PegawaiRequest request = new PegawaiRequest(
                "OPD-02",
                2026,
                "Baru",
                nip,
                "baru@contoh",
                null,
                12L,
                "   "
        );
        Pegawai saved = new Pegawai(
                1L,
                "OPD-02",
                2026,
                "Baru",
                nip,
                "baru@contoh",
                "LEVEL_3",
                12L,
                Instant.EPOCH,
                Instant.EPOCH
        );

        when(pegawaiRepository.findByNip(nip)).thenReturn(Optional.of(existing));
        when(pegawaiRepository.save(any())).thenReturn(saved);

        Pegawai result = pegawaiService.ubahPegawai(nip, request);

        assertEquals(saved, result);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(pegawaiAuthRepository);
    }

    @Test
    void ubahPegawai_throwsWhenAuthUpdateFails() {
        String nip = "123456";
        Pegawai existing = new Pegawai(
                1L,
                "OPD-01",
                2025,
                "Awal",
                nip,
                "awal@contoh",
                "LEVEL_2",
                10L,
                Instant.EPOCH,
                Instant.EPOCH
        );
        PegawaiRequest request = new PegawaiRequest(
                "OPD-01",
                2025,
                "Baru",
                nip,
                "baru@contoh",
                "2",
                11L,
                "password-baru"
        );
        Pegawai saved = new Pegawai(
                1L,
                "OPD-01",
                2025,
                "Baru",
                nip,
                "baru@contoh",
                "LEVEL_2",
                11L,
                Instant.EPOCH,
                Instant.EPOCH
        );

        when(pegawaiRepository.findByNip(nip)).thenReturn(Optional.of(existing));
        when(pegawaiRepository.save(any())).thenReturn(saved);
        when(passwordEncoder.encode("password-baru")).thenReturn("hash");
        when(pegawaiAuthRepository.setPasswordHashAndRole(nip, "hash", "LEVEL_2")).thenReturn(0);

        ResponseStatusException actual = assertThrows(ResponseStatusException.class,
                () -> pegawaiService.ubahPegawai(nip, request));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
        assertEquals("Gagal menyimpan kredensial pegawai", actual.getReason());
        verify(passwordEncoder).encode("password-baru");
        verify(pegawaiAuthRepository).setPasswordHashAndRole(nip, "hash", "LEVEL_2");
    }
}
