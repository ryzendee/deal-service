package ryzendee.app.dto;

import java.math.BigDecimal;

/**
 * DTO, представляющее сумму сделки.
 * Содержит числовое значение и валюту.
 *
 * @author Dmitry Ryazantsev
 */
public record DealSumDetails(BigDecimal value, String currency) {

    /**
     * Возвращает строковое представление значения суммы в plain формате.
     *
     * @return строковое представление значения суммы
     */
    public String valueToPlainString() {
        return value.toPlainString();
    }
}
