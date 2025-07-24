package ryzendee.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ryzendee.app.dto.DealContractorRoleAddRequest;
import ryzendee.app.dto.DealContractorRoleRemoveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.models.*;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ryzendee.app.service.DealTestHelper.*;

public class DealContractorToRoleServiceIT extends AbstractServiceIT {

    @Autowired
    private DealContractorToRoleService dealContractorToRoleService;

    private Deal deal;
    private DealContractor contractor;
    private ContractorRole role;

    @BeforeEach
    void setUp() {
        databaseUtil.cleanDatabase();
        deal = createAndSaveTestDeal(databaseUtil);
        contractor = createAndSaveContractor(databaseUtil, deal);
        role = createAndSaveRole(databaseUtil);
    }

    @Test
    void addRoleToContractor_validRequest_shouldSaveRelation() {
        DealContractorRoleAddRequest request = DealContractorRoleAddRequest.builder()
                .dealContractorId(contractor.getId())
                .roleId(role.getId())
                .build();

        dealContractorToRoleService.addRoleToContractor(request);

        ContractorToRoleId relationId = buildRoleId(contractor.getId(), role.getId());
        ContractorToRoleRelation relation = databaseUtil.find(relationId, ContractorToRoleRelation.class);
        assertThat(relation).isNotNull();
        assertThat(relation.isActive()).isTrue();
        assertThat(relation.getContractorRole().getId()).isEqualTo(role.getId());
    }

    @Test
    void addRoleToContractor_invalidContractor_shouldThrow() {
        DealContractorRoleAddRequest request = DealContractorRoleAddRequest.builder()
                .dealContractorId(randomUUID())
                .roleId(role.getId())
                .build();

        assertThatThrownBy(() -> dealContractorToRoleService.addRoleToContractor(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addRoleToContractor_invalidRole_shouldThrow() {
        DealContractorRoleAddRequest request = DealContractorRoleAddRequest.builder()
                .dealContractorId(contractor.getId())
                .roleId("dummy")
                .build();

        assertThatThrownBy(() -> dealContractorToRoleService.addRoleToContractor(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteRole_existingRelation_shouldDeactivate() {
        ContractorToRoleRelation relation = saveRelation(contractor, role);
        DealContractorRoleRemoveRequest request = DealContractorRoleRemoveRequest.builder()
                .dealContractorId(contractor.getId())
                .roleId(role.getId())
                .build();

        dealContractorToRoleService.deleteRole(request);

        ContractorToRoleRelation deleted = databaseUtil.find(relation.getContractorToRoleId(), ContractorToRoleRelation.class);
        assertThat(deleted).isNotNull();
        assertThat(deleted.isActive()).isFalse();
    }

    @Test
    void deleteRole_nonExistingRelation_shouldThrow() {
        DealContractorRoleRemoveRequest request = DealContractorRoleRemoveRequest.builder()
                .dealContractorId(contractor.getId())
                .roleId("dummy")
                .build();

        assertThatThrownBy(() -> dealContractorToRoleService.deleteRole(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private ContractorToRoleRelation saveRelation(DealContractor contractor, ContractorRole role) {
        ContractorToRoleRelation relation = ContractorToRoleRelation.builder()
                .contractorToRoleId(buildRoleId(contractor.getId(), role.getId()))
                .contractorRole(role)
                .dealContractor(contractor)
                .build();

        return databaseUtil.merge(relation);
    }

    private ContractorToRoleId buildRoleId(UUID contractorId, String roleId) {
        return ContractorToRoleId.builder()
                .contractorId(contractorId)
                .roleId(roleId)
                .build();
    }
}
