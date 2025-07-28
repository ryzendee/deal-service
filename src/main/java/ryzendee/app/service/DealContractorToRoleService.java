package ryzendee.app.service;

import ryzendee.app.dto.DealContractorRoleAddRequest;
import ryzendee.app.dto.DealContractorRoleRemoveRequest;

/**
 * Сервис для управления ролями контрагентов в рамках сделок.
 *
 * @author Dmitry Ryazantsev
 */
public interface DealContractorToRoleService {

    /**
     * Добавляет новую роль существующему контрагенту сделки.
     *
     * @param request объект с данными для добавления роли
     */
    void addRoleToContractor(DealContractorRoleAddRequest request);

    /**
     * Логически удаляет роль у контрагента сделки.
     *
     * @param request объект с данными для удаления роли
     */
    void deleteRole(DealContractorRoleRemoveRequest request);
}
