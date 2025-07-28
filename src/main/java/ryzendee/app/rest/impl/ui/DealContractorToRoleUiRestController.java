package ryzendee.app.rest.impl.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.DealContractorRoleAddRequest;
import ryzendee.app.dto.DealContractorRoleRemoveRequest;
import ryzendee.app.rest.api.ui.DealContractorToRoleUiApi;
import ryzendee.app.service.DealContractorToRoleService;

@RestController
@RequiredArgsConstructor
public class DealContractorToRoleUiRestController implements DealContractorToRoleUiApi {

    private final DealContractorToRoleService dealContractorToRoleService;

    @PreAuthorize("@dealAccessRules.canEditDeals(authentication)")
    @Override
    public void addRoleToContractor(DealContractorRoleAddRequest request) {
        dealContractorToRoleService.addRoleToContractor(request);
    }

    @PreAuthorize("@dealAccessRules.canEditDeals(authentication)")
    @Override
    public void deleteRoleFromContractor(DealContractorRoleRemoveRequest request) {
        dealContractorToRoleService.deleteRole(request);
    }
}
