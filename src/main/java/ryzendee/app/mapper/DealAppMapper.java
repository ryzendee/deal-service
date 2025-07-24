package ryzendee.app.mapper;

import org.mapstruct.*;
import ryzendee.app.dto.DealDetails;
import ryzendee.app.dto.DealSaveRequest;
import ryzendee.app.models.Deal;

/**
 * Маппер для преобразования данных сделки.
 * Используется MapStruct.
 *
 * @author Dmitry Ryazantsev
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DealAppMapper {

    /**
     * Преобразует запрос в модель Deal.
     *
     * @param request объект запроса
     * @return модель сделки
     */
    Deal toModel(DealSaveRequest request);

    /**
     * Преобразует модель Deal в DTO.
     *
     * @param model модель сделки
     * @return DTO сделки
     */
    DealDetails toDetails(Deal model);

    /**
     * Обновляет модель сделки данными из запроса.
     *
     * @param data данные для обновления
     * @param toUpdate обновляемая модель
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "modifyDate", ignore = true)
    @Mapping(target = "createUserId", ignore = true)
    @Mapping(target = "modifyUserId", ignore = true)
    void updateFromRequest(DealSaveRequest data, @MappingTarget Deal toUpdate);
}
