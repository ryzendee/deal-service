package ryzendee.app.rest.api.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ryzendee.app.dto.ContractorDetails;
import ryzendee.app.dto.ContractorSaveRequest;

import java.util.UUID;

@RequestMapping("/ui/deal-contractor")
@Tag(name = "UI API контрагентов сделки", description = "Операции с контрагентами, привязанными к сделке")
public interface DealContractorUiApi {

    @Operation(
            summary = "Сохранить или обновить контрагента сделки",
            description = "Создает нового или обновляет существующего контрагента в рамках сделки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Контрагент сделки успешно сохранён"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
            }
    )
    @PutMapping("/save")
    ContractorDetails saveOrUpdateDealContractor(@Valid @RequestBody ContractorSaveRequest request);

    @Operation(
            summary = "Удалить контрагента сделки",
            description = "Логически удаляет контрагента из сделки (is_active = false)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Контрагент сделки удалён"),
                    @ApiResponse(responseCode = "404", description = "Контрагент сделки не найден"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    void deleteDealContractor(@RequestParam("id") UUID id);
}
