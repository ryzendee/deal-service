package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ryzendee.app.models.DealStatus;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью DealStatus.
 *
 * @author Dmitry Ryazantsev
 */
public interface DealStatusRepository extends JpaRepository<DealStatus, String> {

    /**
     * Находит статус сделки по имени.
     *
     * @param name имя статуса
     * @return Optional со статусом или пустой, если не найден
     */
    Optional<DealStatus> findByName(String name);
}
