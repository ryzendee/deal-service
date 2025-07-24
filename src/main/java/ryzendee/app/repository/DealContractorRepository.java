package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ryzendee.app.models.DealContractor;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью DealContractor.
 *
 * @author Dmitry Ryazantsev
 */
public interface DealContractorRepository extends JpaRepository<DealContractor, UUID> {

}
