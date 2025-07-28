package ryzendee.app.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Модель суммы сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DealSum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 100, scale = 2)
    private BigDecimal sum;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isMain = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal;

}
