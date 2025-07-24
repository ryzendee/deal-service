package ryzendee.app.service;

import org.springframework.data.domain.Page;
import ryzendee.app.dto.DealDetails;
import ryzendee.app.dto.DealSaveRequest;
import ryzendee.app.dto.DealSearchFilter;
import ryzendee.app.dto.DealStatusChangeRequest;
import ryzendee.app.util.exporter.ExportResult;

import java.util.UUID;

/**
 * Сервис для работы со сделками.
 *
 * @author Dmitry Ryazantsev
 */
public interface DealService {

    /**
     * Сохраняет новую или обновляет существующую сделку.
     *
     * @param request объект с данными сделки
     * @return информация о сохраненной/обновленной сделке
     */
    DealDetails saveOrUpdate(DealSaveRequest request);

    /**
     * Изменяет статус существующей сделки.
     *
     * @param request объект с параметрами изменения статуса
     */
    void changeDealStatus(DealStatusChangeRequest request);

    /**
     * Получает полную информацию о сделке по её уникальному идентификатору.
     *
     * @param id уникальный идентификатор сделки
     * @return DealDetails с полной информацией о сделке
     */
    DealDetails getDealById(UUID id);

    /**
     * Выполняет постраничный поиск сделок с учетом фильтров и сортировки.
     *
     * @param filter фильтр для поиска сделок
     * @return Page с найденными сделками
     */
    Page<DealDetails> searchDeals(DealSearchFilter filter);

    /**
     * Экспортирует список активных сделок, соответствующих фильтру
     *
     * @param filter фильтр для поиска сделок
     * @return ExportResult с содержимым экспорта
     */
    ExportResult exportDetails(DealSearchFilter filter);
}
