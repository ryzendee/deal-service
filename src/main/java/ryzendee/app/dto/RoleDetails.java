package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO с деталями роли, включая её идентификатор, название и категорию.
 *
 * @author Dmitry Ryazantsev
 */
@Schema(description = "Детальная информация о роли контрагента")
public record RoleDetails(
        @Schema(description = "Идентификатор роли", example = "role123")
        String id,

        @Schema(description = "Название роли", example = "Заёмщик")
        String name,

        @Schema(description = "Категория роли", example = "Основная")
        String category
) {
}
