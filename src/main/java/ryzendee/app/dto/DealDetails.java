package ryzendee.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Детальная информация о сделке.
 *
 * @author Dmitry Ryazantsev
 */
public record DealDetails(
        UUID id,
        String description,
        String agreementNumber,
        LocalDate agreementDate,
        LocalDate agreementStartDate,
        LocalDate availabilityDate,
        DealTypeDetails type,
        DealStatusDetails status,
        DealSumDetails sum,
        LocalDateTime closeDate,
        List<ContractorDetails> contractors
) {
}
