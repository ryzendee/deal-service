package ryzendee.app.mapper;

import org.mapstruct.*;
import ryzendee.app.dto.DealTypeDetails;
import ryzendee.app.dto.DealTypeSaveRequest;
import ryzendee.app.models.DealType;

/**
 * Маппер для преобразования данных типов сделок.
 * Используется MapStruct.
 *
 * @author Dmitry Ryazantsev
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)

public interface DealTypeAppMapper {

    /**
     * Преобразует запрос в модель DealType.
     *
     * @param request объект запроса
     * @return модель типа сделки
     */
    DealType toModel(DealTypeSaveRequest request);

    /**
     * Преобразует модель DealType в DTO.
     *
     * @param model модель типа сделки
     * @return DTO типа сделки
     */
    DealTypeDetails toDetails(DealType model);

    /**
     * Обновляет модель типа сделки данными из запроса.
     *
     * @param data данные для обновления
     * @param toUpdate обновляемая модель
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateFromRequest(DealTypeSaveRequest data, @MappingTarget DealType toUpdate);

}
