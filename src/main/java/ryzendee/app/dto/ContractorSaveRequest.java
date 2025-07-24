package ryzendee.app.dto;

import lombok.Builder;

import java.util.UUID;

/**
 * DTO для сохранения или обновления контрагента сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
public record ContractorSaveRequest(
        UUID id,
        UUID dealId,
        String contractorId,
        String name,
        String inn,
        boolean main
) {
}
