package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO, представляющее статус сделки.
 * Содержит идентификатор и название статуса.
 *
 * @author Dmitry Ryazantsev
 */
@Schema(description = "Детальная информация о статусе сделки")
public record DealStatusDetails(

        @Schema(description = "Идентификатор статуса", example = "1")
        String id,

        @Schema(description = "Название статуса", example = "APPROVED")
        String name
) {
}
