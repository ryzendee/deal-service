package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * DTO для сохранения или обновления контрагента сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
@Schema(description = "Запрос на создание или обновление контрагента сделки")
public record ContractorSaveRequest(

        @Schema(description = "Идентификатор записи (null при создании)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "ID сделки, к которой относится контрагент", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "ID сделки не может быть пустым")
        UUID dealId,

        @Schema(description = "ID контрагента", example = "C123456789")
        @NotBlank(message = "ID контрагента не может быть пустым")
        String contractorId,

        @Schema(description = "Наименование контрагента", example = "ООО Альфа")
        @NotBlank(message = "Имя контрагента не может быть пустым")
        String name,

        @Schema(description = "ИНН контрагента", example = "7701234567")
        @NotBlank(message = "ИНН контрагента не может быть пустым")
        String inn,

        @Schema(description = "Признак основного контрагента", example = "true")
        boolean main
) {
}
