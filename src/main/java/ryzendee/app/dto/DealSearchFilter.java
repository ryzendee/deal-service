package ryzendee.app.dto;

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
public record DealSearchFilter(
        UUID dealId,
        String description,
        String agreementNumber,
        LocalDate agreementDateFrom,
        LocalDate agreementDateTo,
        LocalDate availabilityDateFrom,
        LocalDate availabilityDateTo,
        List<String> typeList,
        List<String> statusList,
        LocalDate closeDateFrom,
        LocalDate closeDateTo,
        String borrowerSearch,
        String warranitySearch,
        int page,
        int size,
        SumFilter sum
) {
    /**
     * Вложенный фильтр для суммы с указанием значения и валюты.
     */
    public record SumFilter(BigDecimal value, String currency) {}
}
