package cc.kertaskerja.bontang.laporanverifikasi.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum TahapVerifikasi {
    LEVEL_1,
    LEVEL_2;

    public static TahapVerifikasi fromRaw(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tahapVerifikasi wajib diisi");
        }

        try {
            return TahapVerifikasi.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "tahapVerifikasi tidak valid. Gunakan LEVEL_1 atau LEVEL_2"
            );
        }
    }
}
