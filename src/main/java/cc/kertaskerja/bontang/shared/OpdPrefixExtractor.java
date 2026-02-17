package cc.kertaskerja.bontang.shared;

import java.util.StringJoiner;

public final class OpdPrefixExtractor {
    private OpdPrefixExtractor() {}

    public static String extractPrefix(String kodeOpd, int segments) {
        if (kodeOpd == null || kodeOpd.isBlank() || segments <= 0) {
            return null;
        }

        String[] parts = kodeOpd.split("\\.");
        StringJoiner joiner = new StringJoiner(".");
        int taken = 0;

        for (String part : parts) {
            if (taken == segments) {
                break;
            }

            String trimmed = part.strip();
            if (trimmed.isEmpty()) {
                continue;
            }

            joiner.add(trimmed);
            taken++;
        }

        return taken == 0 ? null : joiner.toString();
    }
}
