package ryzendee.app.service;

import ryzendee.app.dto.ContractorDetails;
import ryzendee.app.dto.ContractorSaveRequest;

import java.util.UUID;

/**
 * Сервис для управления контрагентами в рамках сделки.
 *
 * @author Dmitry Ryazantsev
 */
public interface DealContractorService {

    /**
     * Сохраняет нового или обновляет существующего контрагента сделки.
     * Если идентификатор существует — обновляет данные, иначе создаёт нового.
     *
     * @param request DTO с данными контрагента для сохранения
     * @return ContractorDetails с актуальной информацией по контрагенту
     */
    ContractorDetails saveOrUpdate(ContractorSaveRequest request);

    /**
     * Выполняет логическое удаление контрагента сделки по его идентификатору.
     *
     * @param id уникальный идентификатор контрагента сделки
     */
    void deleteById(UUID id);
}
