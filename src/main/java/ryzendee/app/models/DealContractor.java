package ryzendee.app.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Модель контрагента сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DealContractor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal;

    @Column(length = 12, nullable = false)
    private String contractorId;

    @Column(nullable = false)
    private String name;

    private String inn;

    @Column(nullable = false)
    @Builder.Default
    private Boolean main = false;

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

    @OneToMany(mappedBy = "dealContractor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ContractorToRoleRelation> roles = new ArrayList<>();

}
