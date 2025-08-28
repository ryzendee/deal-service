package ryzendee.app.rest.api.base;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ryzendee.app.dto.*;

import java.util.List;

@RequestMapping("/deal-type")
@Tag(name = "API типов сделок", description = "Операции, связанные с управлением типов сделок")
public interface DealTypeApi {

    @Operation(
            summary = "Сохранить или обновить тип сделки",
            description = "Создает новый тип сделки или обновляет существующий.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Тип сделки успешно сохранен или обновлен"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации данных")
            }
    )
    @PutMapping("/save")
    DealTypeDetails saveOrUpdateDeal(@Valid @RequestBody DealTypeSaveRequest request);

    @Operation(
            summary = "Список всех типов сделок",
            description = "Возвращает информацию о всех имеющихся типах сделок",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Возвращает список типов сделок")
            }
    )
    @GetMapping("/all")
    List<DealTypeDetails> getAllDealTypes();
}
