package cc.kertaskerja.bontang.auth.domain;

import org.springframework.data.relational.core.mapping.Column;

public record PegawaiAuthView(
        String nip,
        @Column("password_hash")
        String passwordHash,
        String role
) {
}

