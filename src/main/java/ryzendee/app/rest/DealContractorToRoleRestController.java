package ryzendee.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ryzendee.app.dto.DealContractorRoleAddRequest;
import ryzendee.app.dto.DealContractorRoleRemoveRequest;
import ryzendee.app.rest.api.DealContractorToRoleApi;
import ryzendee.app.service.DealContractorToRoleService;

@RestController
@RequiredArgsConstructor
public class DealContractorToRoleRestController implements DealContractorToRoleApi {

    private final DealContractorToRoleService dealContractorToRoleService;

    @Override
    public void addRoleToContractor(DealContractorRoleAddRequest request) {
        dealContractorToRoleService.addRoleToContractor(request);
    }

    @Override
    public void deleteRoleFromContractor(DealContractorRoleRemoveRequest request) {
        dealContractorToRoleService.deleteRole(request);
    }
}
