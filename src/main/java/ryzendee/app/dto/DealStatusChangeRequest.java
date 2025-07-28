package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * DTO для запроса на изменение статуса сделки.
 * Содержит идентификатор сделки и нового статуса.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
@Schema(description = "Запрос на изменение статуса сделки")
public record DealStatusChangeRequest(

        @Schema(description = "ID сделки", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "ID сделки не может быть пустым")
        UUID dealId,

        @Schema(description = "ID нового статуса", example = "3fa85f64-5717-4562")
        @NotBlank(message = "ID статуса не может быть пустым")
        String statusId
) {
}
