package cc.kertaskerja.bontang.laporanverifikasi.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public enum LaporanJenis {
    PROGRAM_PRIORITAS,
    RINCIAN_BELANJA;

    public static LaporanJenis fromRaw(String value) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "jenisLaporan wajib diisi");
        }

        try {
            return LaporanJenis.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "jenisLaporan tidak valid. Gunakan PROGRAM_PRIORITAS atau RINCIAN_BELANJA"
            );
        }
    }
}
