package ryzendee.app.rest.api.base;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ryzendee.app.dto.DealStatusDetails;

import java.util.List;

@RequestMapping("/deal-status")
@Tag(name = "API статусов сделок", description = "Операции, связанные с управлением статусом сделок")
public interface DealStatusApi {

    @Operation(
            summary = "Список всех статусов сделок",
            description = "Возвращает информацию о всех имеющихся статусах сделок",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Возвращает список статусов сделок")
            }
    )
    @GetMapping("/all")
    List<DealStatusDetails> getAllDealStatuses();

}
