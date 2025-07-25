package ryzendee.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ryzendee.app.dto.ContractorDetails;
import ryzendee.app.dto.ContractorSaveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.models.Deal;
import ryzendee.app.models.DealContractor;
import ryzendee.app.service.DealContractorService;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ryzendee.app.service.DealTestHelper.createAndSaveContractor;
import static ryzendee.app.service.DealTestHelper.createAndSaveTestDeal;
import static ryzendee.app.testutils.FixtureUtil.contractorSaveRequestBuilderFixture;

public class DealContractorServiceIT extends AbstractServiceIT {

    @Autowired
    private DealContractorService dealContractorService;

    private Deal existingDeal;

    @BeforeEach
    void setUp() {
        databaseUtil.cleanDatabase();
        existingDeal = createAndSaveTestDeal(databaseUtil);
    }

    @Test
    void saveOrUpdate_newContractor_shouldSaveAndReturnDetails() {
        ContractorSaveRequest request = contractorSaveRequestBuilderFixture()
                .dealId(existingDeal.getId())
                .build();

        ContractorDetails details = dealContractorService.saveOrUpdate(request);

        DealContractor saved = databaseUtil.find(details.id(), DealContractor.class);
        assertThat(saved).isNotNull();
        assertContractorEqualsRequest(saved, request);
        assertThat(saved.getDeal().getId()).isEqualTo(existingDeal.getId());
    }

    @Test
    void saveOrUpdate_existingContractor_shouldUpdateAndReturnDetails() {
        DealContractor contractor = createAndSaveContractor(databaseUtil, existingDeal);

        ContractorSaveRequest request = ContractorSaveRequest.builder()
                .id(contractor.getId())
                .dealId(existingDeal.getId())
                .contractorId(contractor.getContractorId())
                .name("Updated Name")
                .inn("9876543210")
                .main(false)
                .build();

        ContractorDetails details = dealContractorService.saveOrUpdate(request);

        DealContractor updated = databaseUtil.find(details.id(), DealContractor.class);
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(request.name());
        assertThat(updated.getInn()).isEqualTo(request.inn());
        assertThat(updated.getMain()).isFalse();
    }

    @Test
    void saveOrUpdate_withInvalidDeal_shouldThrow() {
        ContractorSaveRequest request = contractorSaveRequestBuilderFixture().build();

        assertThatThrownBy(() -> dealContractorService.saveOrUpdate(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteById_existingContractor_shouldMarkAsInactive() {
        DealContractor contractor = createAndSaveContractor(databaseUtil, existingDeal);

        dealContractorService.deleteById(contractor.getId());

        DealContractor deleted = databaseUtil.find(contractor.getId(), DealContractor.class);
        assertThat(deleted).isNotNull();
        assertThat(deleted.getIsActive()).isFalse();
    }

    @Test
    void deleteById_nonExistingContractor_shouldThrow() {
        assertThatThrownBy(() -> dealContractorService.deleteById(randomUUID()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private void assertContractorEqualsRequest(DealContractor contractor, ContractorSaveRequest request) {
        assertThat(contractor.getContractorId()).isEqualTo(request.contractorId());
        assertThat(contractor.getName()).isEqualTo(request.name());
        assertThat(contractor.getInn()).isEqualTo(request.inn());
        assertThat(contractor.getMain()).isEqualTo(request.main());
    }
}
