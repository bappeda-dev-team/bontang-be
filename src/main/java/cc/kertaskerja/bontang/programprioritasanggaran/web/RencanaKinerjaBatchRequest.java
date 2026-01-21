package cc.kertaskerja.bontang.programprioritasanggaran.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class RencanaKinerjaBatchRequest {

    @Valid
    @NotNull(message = "rencanaKinerja tidak boleh null")
    private List<RencanaKinerjaItem> rencanaKinerja;

    public List<RencanaKinerjaItem> getRencanaKinerja() {
        return rencanaKinerja;
    }

    public void setRencanaKinerja(List<RencanaKinerjaItem> rencanaKinerja) {
        this.rencanaKinerja = rencanaKinerja;
    }

    public static class RencanaKinerjaItem {
        @NotNull(message = "idRencanaKinerja tidak boleh null")
        private Long idRencanaKinerja;

        public Long getIdRencanaKinerja() {
            return idRencanaKinerja;
        }

        public void setIdRencanaKinerja(Long idRencanaKinerja) {
            this.idRencanaKinerja = idRencanaKinerja;
        }
    }
}
