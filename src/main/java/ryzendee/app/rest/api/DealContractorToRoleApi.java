package ryzendee.app.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ryzendee.app.dto.DealContractorRoleAddRequest;
import ryzendee.app.dto.DealContractorRoleRemoveRequest;

@RequestMapping("/contractor-to-role")
@Tag(name = "API ролей контрагентов", description = "Операции по добавлению и удалению ролей у контрагентов сделки")
public interface DealContractorToRoleApi {

    @Operation(
            summary = "Добавить роль контрагенту сделки",
            description = "Добавляет указанную роль к контрагенту сделки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роль успешно добавлена"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
            }
    )
    @PostMapping("/add")
    void addRoleToContractor(@Valid @RequestBody DealContractorRoleAddRequest request);

    @Operation(
            summary = "Удалить роль у контрагента сделки",
            description = "Логически удаляет роль у контрагента (is_active = false)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Роль успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Роль у контрагента не найдена")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    void deleteRoleFromContractor(@RequestBody DealContractorRoleRemoveRequest request);
}
