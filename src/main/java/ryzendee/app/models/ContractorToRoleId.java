package ryzendee.app.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Композитный ключ для связи Контрагента и Роли.
 * Используется как идентификатор для сущности ContractorToRoleRelation.
 *
 * @author Dmitry Ryazantsev
 */
@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractorToRoleId implements Serializable {

    private UUID contractorId;
    private String roleId;

}
