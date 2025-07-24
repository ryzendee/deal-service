package ryzendee.app.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для создания или обновления сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
public record DealSaveRequest(
        UUID id,
        String description,
        String agreementNumber,
        LocalDate agreementDate,
        LocalDateTime agreementStartDate,
        LocalDate availabilityDate,
        String typeId,
        LocalDateTime closeDate,
        DealSumDetails sum
) {
}
