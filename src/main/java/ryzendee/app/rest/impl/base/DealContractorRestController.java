package ryzendee.app.rest.impl.base;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.ContractorDetails;
import ryzendee.app.dto.ContractorSaveRequest;
import ryzendee.app.rest.api.base.DealContractorApi;
import ryzendee.app.service.DealContractorService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DealContractorRestController implements DealContractorApi {

    private final DealContractorService dealContractorService;

    @Override
    public ContractorDetails saveOrUpdateDealContractor(ContractorSaveRequest request) {
        return dealContractorService.saveOrUpdate(request);
    }

    @Override
    public void deleteDealContractor(UUID id) {
        dealContractorService.deleteById(id);
    }
}
