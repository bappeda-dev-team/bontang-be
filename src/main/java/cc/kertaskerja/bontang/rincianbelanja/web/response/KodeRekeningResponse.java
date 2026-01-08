package cc.kertaskerja.bontang.rincianbelanja.web.response;

import cc.kertaskerja.bontang.rinciananggaran.koderekeningrinciananggaran.domain.KodeRekeningRincianAnggaran;

public record KodeRekeningResponse(
    Long idKodeRekening,
    String kodeRekening,
    String namaRekening
) {
    public static KodeRekeningResponse from(KodeRekeningRincianAnggaran kodeRekening) {
        return new KodeRekeningResponse(
            kodeRekening.idKodeRekening(),
            kodeRekening.kodeRekening(),
            kodeRekening.namaRekening()
        );
    }
}