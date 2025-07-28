package ryzendee.app.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * Связь между Контрагентом и Ролью.
 * Использует составной ключ ContractorToRoleId.
 *
 * @author Dmitry Ryazantsev
 */
@Table(name = "contractor_to_role")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractorToRoleRelation {

    @EmbeddedId
    private ContractorToRoleId contractorToRoleId;

    @ManyToOne
    @JoinColumn(name = "contractor_id")
    @MapsId("contractorId")
    private DealContractor dealContractor;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @MapsId("roleId")
    private ContractorRole contractorRole;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

}
