package ryzendee.app.dto;

import lombok.Builder;

import java.util.UUID;

/**
 * DTO для удаления роли у контрагента сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
public record DealContractorRoleRemoveRequest(UUID dealContractorId, String roleId) {
}
