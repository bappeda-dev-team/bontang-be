package cc.kertaskerja.bontang.target.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table(name = "target")
public record Target(
        @Id
        Long id,

        String target,

        String satuan,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate
) {
    public static Target of (
            String target,
            String satuan
    ) {
        return new Target(
                null,
                target,
                satuan,
                null,
                null
        );
    }
}