package ryzendee.app.rest.impl.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.ContractorDetails;
import ryzendee.app.dto.ContractorSaveRequest;
import ryzendee.app.rest.api.base.DealContractorApi;
import ryzendee.app.rest.api.ui.DealContractorUiApi;
import ryzendee.app.service.DealContractorService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DealContractorUiRestController implements DealContractorUiApi {

    private final DealContractorService dealContractorService;

    @PreAuthorize("@dealAccessRules.canEditDeals(authentication)")
    @Override
    public ContractorDetails saveOrUpdateDealContractor(ContractorSaveRequest request) {
        return dealContractorService.saveOrUpdate(request);
    }

    @PreAuthorize("@dealAccessRules.canEditDeals(authentication)")
    @Override
    public void deleteDealContractor(UUID id) {
        dealContractorService.deleteById(id);
    }
}
