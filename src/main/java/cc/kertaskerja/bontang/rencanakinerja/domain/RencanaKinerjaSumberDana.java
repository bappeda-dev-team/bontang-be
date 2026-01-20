package cc.kertaskerja.bontang.rencanakinerja.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="rencana_kinerja_sumber_dana")
public record RencanaKinerjaSumberDana(
        @Id
        Long id,

        @Column("id_rencana_kinerja")
        Long idRencanaKinerja,

        @Column("id_sumber_dana")
        Long idSumberDana
) {
    public static RencanaKinerjaSumberDana of (
            Long idRencanaKinerja,
            Long idSumberDana
    ) {
        return new RencanaKinerjaSumberDana(
                null,
                idRencanaKinerja,
                idSumberDana
        );
    }
}
