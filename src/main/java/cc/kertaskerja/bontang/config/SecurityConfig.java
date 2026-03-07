package cc.kertaskerja.bontang.config;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.config.Customizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter)
            throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(OPTIONS, "/**").permitAll()
                .requestMatchers(GET, "/**/get-all-*").hasAnyRole("LEVEL_2", "LEVEL_3", "LEVEL_4", "SUPER_ADMIN")
                .requestMatchers("/", "/error").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/auth/me").authenticated()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers(GET, "/laporan/cetak/level3").hasRole("LEVEL_3")
                .requestMatchers(GET, "/laporan/cetak/admin-opd").hasRole("ADMIN_OPD")
                .requestMatchers(GET, "/laporan/verifikasi/status", "/laporan/cetak")
                        .hasAnyRole("LEVEL_1", "LEVEL_2", "LEVEL_3", "LEVEL_4", "SUPER_ADMIN")
                .requestMatchers(POST, "/laporan/verifikasi").hasAnyRole("LEVEL_1", "LEVEL_2")
                .requestMatchers(GET, "/laporan/verified/program-prioritas", "/laporan/verified/rincian-belanja")
                        .hasRole("LEVEL_1")
                .requestMatchers(GET, "/laporanrincianbelanja/detail/all-opd/tahun/*").hasRole("SUPER_ADMIN")
                .requestMatchers(GET, "/laporanprogramprioritas/detail/all-opd").hasRole("SUPER_ADMIN")
                .requestMatchers(
                        GET,
                        "/laporanprogramprioritas/detail/kodeopd/*/tahun/*/nip/*",
                        "/laporanrincianbelanja/detail/kodeopd/*/tahun/*/nip/*"
                ).hasRole("LEVEL_3")

                // LEVEL 2+ resource access
                .requestMatchers("/opd/detail/findall").hasAnyRole("LEVEL_1", "LEVEL_2", "LEVEL_3", "LEVEL_4", "SUPER_ADMIN", "ADMIN_OPD")
                .requestMatchers(
                        "/opd/**",
                        "/program/**",
                        "/kegiatan/**",
                        "/subkegiatan/**",
                        "/jabatan/**",
                        "/sumberdana/**",
                        "/koderekening/**",
                        "/pegawai/**",
                        "/bidangurusan/**",
                        "/programprioritas/**"
                ).hasAnyRole("LEVEL_2", "LEVEL_3", "SUPER_ADMIN", "ADMIN_OPD")
                .requestMatchers(
                        "/programopd/**",
                        "/kegiatanopd/**",
                        "/subkegiatanopd/**"
                ).hasAnyRole("LEVEL_2", "LEVEL_3", "SUPER_ADMIN", "ADMIN_OPD")

                // LEVEL 1 restricted endpoints
                .requestMatchers("/programprioritasanggaran/detail/kode-opd/*")
                        .hasAnyRole("LEVEL_1", "LEVEL_2", "ADMIN_OPD")
                .requestMatchers("/rincianbelanja/detail/pegawai/*/*/*")
                        .hasRole("LEVEL_1")
                .requestMatchers(
                        "/laporanprogramprioritas/**",
                        "/laporanrincianbelanja/**"
                ).hasAnyRole("LEVEL_1", "LEVEL_2", "LEVEL_3", "LEVEL_4", "SUPER_ADMIN")

                .requestMatchers("/programprioritasanggaran/**").hasAnyRole("LEVEL_2", "LEVEL_3", "LEVEL_4", "SUPER_ADMIN")

                // LEVEL 2 & 3
                .requestMatchers(
                        "/rencanakinerja/**",
                        "/rencanaaksi/**",
                        "/pelaksanaan-renaksi/**",
                        "/subkegiatanrencanakinerja/**",
                        "/dasarhukum/**",
                        "/gambaranumum/**",
                        "/rincianbelanja/**",
                        "/indikator/**",
                        "/target/**",
                        "/indikatorbelanja/**",
                        "/targetbelanja/**"
                ).hasAnyRole("LEVEL_2", "LEVEL_3", "SUPER_ADMIN")

                // LEVEL 4: login only (via /auth/me), everything else denied by default.
                .anyRequest().denyAll()
            );

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "https://kertaskerjatest.zeabur.app")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
