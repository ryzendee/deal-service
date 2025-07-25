package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для представления типа сделки.
 * Содержит идентификатор и название типа.
 *
 * @author Dmitry Ryazantsev
 */
@Schema(description = "Информация о типе сделки")
public record DealTypeDetails(
        @Schema(description = "Идентификатор типа сделки", example = "type123")
        String id,

        @Schema(description = "Название типа сделки", example = "Кредит")
        String name
) {
}
