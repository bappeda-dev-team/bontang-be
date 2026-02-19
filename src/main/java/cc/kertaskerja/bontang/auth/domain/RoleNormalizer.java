package cc.kertaskerja.bontang.auth.domain;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class RoleNormalizer {
    private static final Set<String> LEVEL_SET = Set.of("LEVEL_1", "LEVEL_2", "LEVEL_3", "LEVEL_4");

    public String normalize(String role) {
        if (role == null || role.isBlank()) {
            return "LEVEL_4";
        }

        String normalized = role.trim().toUpperCase();
        if (normalized.matches("^[1-4]$")) {
            normalized = "LEVEL_" + normalized;
        }

        if (!LEVEL_SET.contains(normalized)) {
            return "LEVEL_4";
        }

        return normalized;
    }
}
