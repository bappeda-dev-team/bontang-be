package cc.kertaskerja.bontang.auth.web;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.kertaskerja.bontang.auth.domain.AuthService;
import cc.kertaskerja.bontang.auth.domain.RoleNormalizer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("auth")
public class AuthController {
    private final AuthService authService;
    private final RoleNormalizer roleNormalizer;

    public AuthController(AuthService authService, RoleNormalizer roleNormalizer) {
        this.authService = authService;
        this.roleNormalizer = roleNormalizer;
    }

    @PostMapping("login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        AuthService.LoginResult result = authService.login(request.nip(), request.password());
        return new LoginResponse(result.accessToken(), "Bearer", result.expiresInSeconds());
    }

    @GetMapping("me")
    public MeResponse me(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new IllegalStateException("Unsupported authentication type");
        }
        Jwt jwt = jwtAuth.getToken();
        String nip = jwt.getSubject();

        var pegawai = authService.me(nip);
        List<String> authorities = jwtAuth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .sorted()
                .toList();

        return new MeResponse(nip, roleNormalizer.normalize(pegawai.role()), authorities, pegawai.namaPegawai());
    }

    public record LoginRequest(
            @NotBlank String nip,
            @NotBlank String password
    ) {}

    public record LoginResponse(
            String accessToken,
            String tokenType,
            long expiresIn
    ) {}

    public record MeResponse(
            String nip,
            String role,
            List<String> authorities,
            String namaPegawai
    ) {}
}
