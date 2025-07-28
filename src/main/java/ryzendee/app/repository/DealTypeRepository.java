package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ryzendee.app.models.DealType;

/**
 * Репозиторий для работы с сущностью DealType.
 *
 * @author Dmitry Ryazantsev
 */
public interface DealTypeRepository extends JpaRepository<DealType, String> {
}
