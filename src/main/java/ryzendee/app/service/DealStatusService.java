package ryzendee.app.service;

import ryzendee.app.dto.DealStatusDetails;

import java.util.List;

/**
 * Сервис для управления статусами сделок.
 *
 * @author Dmitry Ryazantsev
 */

public interface DealStatusService {

    /**
     * Выполняет возврат всех доступных статусов сделок.
     *
     * @return List со статусами
     */
    List<DealStatusDetails> getAllStatuses();
}
