package ryzendee.app.service;

import ryzendee.app.dto.DealTypeDetails;
import ryzendee.app.dto.DealTypeSaveRequest;

import java.util.List;

/**
 * Сервис для управления типами сделок.
 *
 * @author Dmitry Ryazantsev
 */
public interface DealTypeService {

    /**
     * Сохраняет новую или обновляет существующий тип сделки.
     *
     * @param request объект с данными типа сделки
     * @return информация о сохраненном/обновленном типе сделки
     */

    DealTypeDetails saveOrUpdate(DealTypeSaveRequest request);

    /**
     * Выполняет возврат всех доступных типов сделок.
     *
     * @return List с типами
     */
    List<DealTypeDetails> getAllTypes();
}
