package ryzendee.app.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Модель сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String description;

    private String agreementNumber;

    private LocalDate agreementDate;

    private LocalDateTime agreementStartDate;

    private LocalDate availabilityDate;

    private LocalDateTime closeDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime modifyDate;

    private String createUserId;

    private String modifyUserId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private DealType type;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private DealStatus status;

    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DealSum> sums = new ArrayList<>();

    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DealContractor> contractors = new ArrayList<>();

}
