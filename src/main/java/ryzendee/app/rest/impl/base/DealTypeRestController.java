package ryzendee.app.rest.impl.base;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.DealTypeDetails;
import ryzendee.app.dto.DealTypeSaveRequest;
import ryzendee.app.rest.api.base.DealTypeApi;
import ryzendee.app.service.DealTypeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealTypeRestController implements DealTypeApi {

    private final DealTypeService dealTypeService;

    @Override
    public DealTypeDetails saveOrUpdateDeal(DealTypeSaveRequest request) {
        return dealTypeService.saveOrUpdate(request);
    }

    @Override
    public List<DealTypeDetails> getAllDealTypes() {
        return dealTypeService.getAllTypes();
    }
}
