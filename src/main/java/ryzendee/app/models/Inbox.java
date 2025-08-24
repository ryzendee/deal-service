package ryzendee.app.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.JsonJdbcType;
import ryzendee.app.common.enums.EventType;
import ryzendee.app.common.enums.ProcessingStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность с информацией о сообщении во входящей очереди для обработки.
 *
 * @author Dmitry Ryazantsev
 */
@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Inbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ProcessingStatus status = ProcessingStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime processedAt;
}
