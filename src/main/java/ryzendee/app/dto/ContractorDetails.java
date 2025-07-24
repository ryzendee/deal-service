package ryzendee.app.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

/**
 * DTO с подробной информацией о контрагенте сделки.
 *
 * @author Dmitry Ryazantsev
 */
@Builder
public record ContractorDetails(
        UUID id,
        UUID dealId,
        String contractorId,
        String name,
        String inn,
        boolean main,
        List<RoleDetails> roles
) {
}
