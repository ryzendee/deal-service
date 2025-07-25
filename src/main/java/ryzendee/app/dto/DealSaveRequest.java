package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Запрос на создание или обновление сделки")
public record DealSaveRequest(
        @Schema(description = "ID сделки (null при создании)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "Описание сделки", example = "Финансирование ИТ-проекта")
        String description,

        @Schema(description = "Номер соглашения", example = "AG-2023-001")
        String agreementNumber,

        @Schema(description = "Дата заключения соглашения", example = "2023-01-01")
        LocalDate agreementDate,

        @Schema(description = "Дата начала действия соглашения", example = "2023-01-02T00:00:00")
        LocalDateTime agreementStartDate,

        @Schema(description = "Дата доступности средств", example = "2023-01-03")
        LocalDate availabilityDate,

        @Schema(description = "ID типа сделки", example = "defaultTypeId")
        String typeId,

        @Schema(description = "Дата закрытия сделки", example = "2023-12-31T23:59:00")
        LocalDateTime closeDate,

        @Schema(description = "Сумма сделки")
        DealSumDetails sum
) {
}
