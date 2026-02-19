package cc.kertaskerja.bontang.auth.domain;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cc.kertaskerja.bontang.config.SecurityProperties;
import cc.kertaskerja.bontang.pegawai.domain.Pegawai;
import cc.kertaskerja.bontang.pegawai.domain.PegawaiRepository;

@Service
public class AuthService {
    private final RoleNormalizer roleNormalizer;
    private final PegawaiAuthRepository pegawaiAuthRepository;
    private final PegawaiRepository pegawaiRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final SecurityProperties securityProperties;

    public AuthService(
            RoleNormalizer roleNormalizer,
            PegawaiAuthRepository pegawaiAuthRepository,
            PegawaiRepository pegawaiRepository,
            PasswordEncoder passwordEncoder,
            JwtEncoder jwtEncoder,
            SecurityProperties securityProperties
    ) {
        this.roleNormalizer = roleNormalizer;
        this.pegawaiAuthRepository = pegawaiAuthRepository;
        this.pegawaiRepository = pegawaiRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.securityProperties = securityProperties;
    }

    public LoginResult login(String nip, String password) {
        PegawaiAuthView auth = pegawaiAuthRepository.findAuthByNip(nip)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NIP atau password salah"));

        if (auth.passwordHash() == null || auth.passwordHash().isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NIP atau password salah");
        }

        if (!passwordEncoder.matches(password, auth.passwordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "NIP atau password salah");
        }

        String normalizedRole = roleNormalizer.normalize(auth.role());
        Instant now = Instant.now();
        Instant exp = now.plus(securityProperties.getJwt().getTtl());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(nip)
                .issuedAt(now)
                .expiresAt(exp)
                .claim("roles", List.of(normalizedRole))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new LoginResult(tokenValue, securityProperties.getJwt().getTtl().toSeconds());
    }

    public Pegawai me(String nip) {
        return pegawaiRepository.findByNip(nip)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pegawai not found"));
    }

    public record LoginResult(String accessToken, long expiresInSeconds) {}
}
