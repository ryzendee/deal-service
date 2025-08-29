package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Детальная информация о сделке.
 *
 * @author Dmitry Ryazantsev
 */
@Schema(description = "Детальная информация о сделке")
@Builder
public record DealDetails(
        @Schema(description = "Идентификатор сделки", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Описание сделки", example = "Финансирование проекта строительства")
        String description,

        @Schema(description = "Номер соглашения", example = "AG-2023-001")
        String agreementNumber,

        @Schema(description = "Дата заключения соглашения", example = "2023-01-01")
        LocalDate agreementDate,

        @Schema(description = "Дата начала действия соглашения", example = "2023-01-15")
        LocalDate agreementStartDate,

        @Schema(description = "Дата доступности средств", example = "2023-01-20")
        LocalDate availabilityDate,

        @Schema(description = "Тип сделки")
        DealTypeDetails type,

        @Schema(description = "Статус сделки")
        DealStatusDetails status,

        @Schema(description = "Сумма сделки")
        DealSumDetails sum,

        @Schema(description = "Дата закрытия сделки", example = "2023-12-31T23:59:00")
        LocalDateTime closeDate,

        @Schema(description = "Список контрагентов, участвующих в сделке")
        List<ContractorDetails> contractors
) {
}
