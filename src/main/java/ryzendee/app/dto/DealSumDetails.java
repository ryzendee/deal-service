package ryzendee.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * DTO, представляющее сумму сделки.
 * Содержит числовое значение и валюту.
 *
 * @author Dmitry Ryazantsev
 */
@Schema(description = "Детальная информация о сумме сделки")
public record DealSumDetails(

        @Schema(description = "Значение суммы", example = "1000.00")
        BigDecimal value,

        @Schema(description = "Валюта суммы", example = "USD")
        String currency
) {
    /**
     * Возвращает строковое представление значения суммы в plain формате.
     *
     * @return строковое представление значения суммы
     */
    public String valueToPlainString() {
        return value.toPlainString();
    }
}
