package ryzendee.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ryzendee.app.dto.ContractorDetails;
import ryzendee.app.dto.ContractorSaveRequest;
import ryzendee.app.models.DealContractor;

/**
 * Маппер для преобразования DealContractor.
 *
 * @author Dmitry Ryazantsev
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DealContractorAppMapper {

    /**
     * Преобразует DTO запроса в модель DealContractor.
     *
     * @param details данные запроса
     * @return модель DealContractor
     */
    DealContractor toModel(ContractorSaveRequest details);

    /**
     * Преобразует модель DealContractor в DTO с деталями.
     *
     * @param model модель DealContractor
     * @return DTO с деталями
     */
    ContractorDetails toDetails(DealContractor model);

    /**
     * Обновляет модель DealContractor данными из запроса.
     *
     * @param request данные для обновления
     * @param toUpdate обновляемая модель
     */
    void updateFromRequest(ContractorSaveRequest request, @MappingTarget DealContractor toUpdate);
}
