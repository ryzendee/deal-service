package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ryzendee.app.models.ContractorToRoleId;
import ryzendee.app.models.ContractorToRoleRelation;

/**
 * Репозиторий для работы с сущностью ContractorToRole.
 *
 * @author Dmitry Ryazantsev
 */
public interface ContractorToRoleRepository extends JpaRepository<ContractorToRoleRelation, ContractorToRoleId> {
}
