package cc.kertaskerja.bontang.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kertaskerja.security")
public class SecurityProperties {
    private boolean setupEnabled = true;
    private final Jwt jwt = new Jwt();

    public boolean isSetupEnabled() {
        return setupEnabled;
    }

    public void setSetupEnabled(boolean setupEnabled) {
        this.setupEnabled = setupEnabled;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public static class Jwt {
        private String secret;
        private Duration ttl = Duration.ofHours(8);

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Duration getTtl() {
            return ttl;
        }

        public void setTtl(Duration ttl) {
            this.ttl = ttl;
        }
    }
}
