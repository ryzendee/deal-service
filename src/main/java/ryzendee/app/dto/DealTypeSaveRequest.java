package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO для представления запроса сохранения типа сделки.
 * Содержит идентификатор и название типа.
 *
 * @author Dmitry Ryazantsev
 */
@Schema(description = "Запрос на сохранение типа сделки")
@Builder
public record DealTypeSaveRequest(

        @Schema(description = "Идентификатор типа сделки", example = "type123")
        String id,

        @Schema(description = "Название типа сделки", example = "Кредит")
        String name
) {
}
