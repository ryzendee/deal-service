package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Фильтр для поиска сделок с возможностью указания диапазонов дат, списков типов и статусов,
 * а также пагинации и фильтра по сумме.
 *
 * @author Dmitry Ryazantsev
 */
@Schema(description = "Фильтр для поиска сделок")
public record DealSearchFilter(
        @Schema(description = "ID сделки", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID dealId,

        @Schema(description = "Описание сделки (поиск по подстроке)", example = "финансирование")
        String description,

        @Schema(description = "Номер соглашения", example = "AG-2023-001")
        String agreementNumber,

        @Schema(description = "Дата соглашения — от", example = "2023-01-01")
        LocalDate agreementDateFrom,

        @Schema(description = "Дата соглашения — до", example = "2023-12-31")
        LocalDate agreementDateTo,

        @Schema(description = "Дата доступности — от", example = "2023-02-01")
        LocalDate availabilityDateFrom,

        @Schema(description = "Дата доступности — до", example = "2023-11-30")
        LocalDate availabilityDateTo,

        @Schema(description = "Список ID типов сделок", example = "[\"type1\", \"type2\"]")
        List<String> typeList,

        @Schema(description = "Список ID статусов сделок", example = "[\"status1\", \"status2\"]")
        List<String> statusList,

        @Schema(description = "Дата закрытия — от", example = "2023-05-01")
        LocalDate closeDateFrom,

        @Schema(description = "Дата закрытия — до", example = "2023-10-31")
        LocalDate closeDateTo,

        @Schema(description = "Поиск по заёмщику", example = "ООО Альфа")
        String borrowerSearch,

        @Schema(description = "Поиск по поручителю", example = "ЗАО Бета")
        String warranitySearch,

        @Schema(description = "Номер страницы (начиная с 0)", example = "0")
        int page,

        @Schema(description = "Размер страницы", example = "10")
        int size,

        @Schema(description = "Фильтр по сумме сделки")
        SumFilter sum
) {
    /**
     * Вложенный фильтр для суммы с указанием значения и валюты.
     */
    @Schema(description = "Фильтр по сумме сделки")
    public record SumFilter(
            @Schema(description = "Сумма сделки", example = "1000.00")
            BigDecimal value,

            @Schema(description = "Валюта сделки", example = "USD")
            String currency
    ) {}
}
