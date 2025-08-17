package ryzendee.app.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ryzendee.app.common.enums.ProcessingStatus;
import ryzendee.app.models.Inbox;

import java.util.List;

/**
 * Репозиторий для работы с {@link Inbox} сущностями.
 *
 * @author Dmitry Ryazantsev
 */

public interface InboxRepository extends JpaRepository<Inbox, Long> {

    /**
     * Находит список сообщений {@link Inbox} с заданным статусом.
     *
     * @param status  статус сообщений, которые необходимо выбрать
     * @param pageable параметры пагинации и сортировки
     * @return список сообщений {@link Inbox} с указанным статусом
     */
    List<Inbox> findAllByStatus(ProcessingStatus status, Pageable pageable);
}
