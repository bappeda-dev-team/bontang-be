package cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.web;

import jakarta.annotation.Nullable;

public record KodeRekeningRincianAnggaranRequest(
        @Nullable
        Long idKodeRekening,

        @Nullable
        String kodeRekening,

        @Nullable
        String namaRekening
) {
}
