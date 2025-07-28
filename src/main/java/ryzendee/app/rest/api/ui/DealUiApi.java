package ryzendee.app.rest.api.ui;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ryzendee.app.dto.DealDetails;
import ryzendee.app.dto.DealSaveRequest;
import ryzendee.app.dto.DealSearchFilter;
import ryzendee.app.dto.DealStatusChangeRequest;

import java.util.UUID;

@RequestMapping("/ui/deal")
@Tag(name = "UI API сделок", description = "Операции, связанные с управлением сделками")
public interface DealUiApi {

    @Operation(
            summary = "Сохранить или обновить сделку",
            description = "Создает новую сделку или обновляет существующую. При создании статус всегда DRAFT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сделка успешно сохранена или обновлена"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
            }
    )
    @PutMapping("/save")
    DealDetails saveOrUpdateDeal(@Valid @RequestBody DealSaveRequest request);

    @Operation(
            summary = "Изменить статус сделки",
            description = "Обновляет статус существующей сделки. Используется только для смены статуса.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статус успешно обновлён"),
                    @ApiResponse(responseCode = "404", description = "Сделка не найдена")
            }
    )
    @PatchMapping("/change/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void changeDealStatus(@Valid @RequestBody DealStatusChangeRequest request);

    @Operation(
            summary = "Получить сделку по ID",
            description = "Возвращает полное описание сделки по идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сделка найдена"),
                    @ApiResponse(responseCode = "404", description = "Сделка не найдена"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
            }
    )
    @GetMapping("/{id}")
    DealDetails getDealById(@PathVariable("id") UUID id);

    @Operation(
            summary = "Поиск сделок",
            description = "Постраничный поиск активных сделок с фильтрацией и сортировкой по различным полям",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Возвращает список сделок"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
            }
    )
    @PostMapping("/search")
    Page<DealDetails> searchDeals(@RequestBody DealSearchFilter filter);

    @Operation(
            summary = "Экспорт сделок в Excel",
            description = "Экспортирует список активных сделок в формате Excel с применением фильтров и сортировки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешный экспорт. Возвращается Excel файл.",
                            headers = {
                                    @Header(name = "Content-Disposition", description = "attachment; filename=deals_export.xlsx")
                            }
                    ),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещён")
            }
    )
    @PostMapping("/search/export")
    ResponseEntity<byte[]> exportDealsToExcel(@RequestBody DealSearchFilter filter);
}


