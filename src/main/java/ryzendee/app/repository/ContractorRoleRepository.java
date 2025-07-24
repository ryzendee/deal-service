package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ryzendee.app.models.ContractorRole;


/**
 * Репозиторий для работы с сущностью ContractorRole.
 *
 * @author Dmitry Ryazantsev
 */
public interface ContractorRoleRepository extends JpaRepository<ContractorRole, String> {
}
