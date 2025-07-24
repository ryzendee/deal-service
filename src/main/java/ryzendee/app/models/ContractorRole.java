package ryzendee.app.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель роли контрагента.
 * Содержит идентификатор, имя, категорию и статус активности.
 * Также хранит связи с ролями контрагентов.
 *
 * @author Dmitry Ryazantsev
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractorRole {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "contractorRole", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ContractorToRoleRelation> contractorToRoleRelationList = new ArrayList<>();
}
