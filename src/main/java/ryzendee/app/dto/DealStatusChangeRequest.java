package ryzendee.app.dto;

import lombok.Builder;

import java.util.UUID;

/**
 * DTO для запроса на изменение статуса сделки.
 * Содержит идентификатор сделки и новый статус.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
public record DealStatusChangeRequest(UUID dealId, String statusId) {
}
