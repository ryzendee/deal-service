package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

/**
 * DTO для удаления роли у контрагента сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
@Schema(description = "Запрос на логическое удаление роли у контрагента сделки")
public record DealContractorRoleRemoveRequest(

        @Schema(description = "ID контрагента, к которому нужно добавить роль", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "ID контрагента не может быть пустым")
        UUID contractorId,

        @Schema(description = "ID роли, которую нужно добавить", example = "BORROWER")
        @NotBlank(message = "ID роли не может быть пустым")
        String roleId

) {
}
