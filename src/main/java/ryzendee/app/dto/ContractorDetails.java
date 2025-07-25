package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * DTO с подробной информацией о контрагенте сделки.
 *
 * @author Dmitry Ryazantsev
 */

@Schema(description = "Подробная информация о контрагенте сделки")
@Builder
public record ContractorDetails(

        @Schema(description = "Идентификатор записи", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "ID сделки, к которой привязан контрагент", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID dealId,

        @Schema(description = "Уникальный идентификатор контрагента", example = "C123456789")
        String contractorId,

        @Schema(description = "Наименование контрагента", example = "ООО Ромашка")
        String name,

        @Schema(description = "ИНН контрагента", example = "1234567890")
        String inn,

        @Schema(description = "Признак основного контрагента", example = "true")
        boolean main,

        @Schema(description = "Список ролей контрагента")
        List<RoleDetails> roles
) {
}
