package ryzendee.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ryzendee.app.dto.DealStatusDetails;
import ryzendee.app.models.DealStatus;

/**
 * Маппер для преобразования данных статусов сделок.
 * Используется MapStruct.
 *
 * @author Dmitry Ryazantsev
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DealStatusAppMapper {


    /**
     * Преобразует модель DealStatus в DTO.
     *
     * @param model модель типа сделки
     * @return DTO типа сделки
     */
    DealStatusDetails toDetails(DealStatus model);
}
