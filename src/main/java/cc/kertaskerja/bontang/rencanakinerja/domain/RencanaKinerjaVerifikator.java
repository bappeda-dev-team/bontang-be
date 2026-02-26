package cc.kertaskerja.bontang.rencanakinerja.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rencana_kinerja_verifikator")
public record RencanaKinerjaVerifikator(
        @Id
        Long id,

        @Column("id_rencana_kinerja")
        Long idRencanaKinerja,

        @Column("nip_verifikator")
        String nipVerifikator
) {
    public static RencanaKinerjaVerifikator of(Long idRencanaKinerja, String nipVerifikator) {
        return new RencanaKinerjaVerifikator(null, idRencanaKinerja, nipVerifikator);
    }
}
