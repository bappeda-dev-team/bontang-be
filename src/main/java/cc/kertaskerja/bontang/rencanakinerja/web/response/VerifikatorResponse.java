package cc.kertaskerja.bontang.rencanakinerja.web.response;

import java.util.LinkedHashMap;
import java.util.Map;

import cc.kertaskerja.bontang.pegawai.domain.Pegawai;

public record VerifikatorResponse(
        Long id,
        String kodeOpd,
        Integer tahun,
        String namaPegawai,
        String nip,
        String email,
        String role,
        Long jabatanId
) {
    public static VerifikatorResponse from(Pegawai pegawai) {
        return new VerifikatorResponse(
                pegawai.id(),
                pegawai.kodeOpd(),
                pegawai.tahun(),
                pegawai.namaPegawai(),
                pegawai.nip(),
                pegawai.email(),
                pegawai.role(),
                pegawai.jabatanId()
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", id);
        map.put("kodeOpd", kodeOpd);
        map.put("tahun", tahun);
        map.put("namaPegawai", namaPegawai);
        map.put("nip", nip);
        map.put("email", email);
        map.put("role", role);
        map.put("jabatanId", jabatanId);
        return map;
    }
}
