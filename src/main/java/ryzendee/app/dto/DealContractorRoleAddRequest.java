package ryzendee.app.dto;

import lombok.Builder;

import java.util.UUID;

/**
 * DTO для добавления роли к контрагенту сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
public record DealContractorRoleAddRequest(UUID dealContractorId, String roleId) {
}
