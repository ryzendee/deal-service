package ryzendee.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ryzendee.app.dto.DealDetails;
import ryzendee.app.dto.DealSaveRequest;
import ryzendee.app.dto.DealStatusChangeRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.models.Deal;
import ryzendee.app.models.DealStatus;
import ryzendee.app.models.DealType;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ryzendee.app.service.DealTestHelper.createAndSaveStatus;
import static ryzendee.app.service.DealTestHelper.createAndSaveTestDeal;
import static ryzendee.app.testutils.FixtureUtil.dealSaveRequestBuilderFixture;

public class DealServiceIT extends AbstractServiceIT {

    @Autowired
    private DealService dealService;

    private Deal existingDeal;

    @BeforeEach
    void setUp() {
        databaseUtil.cleanDatabase();
        existingDeal = createAndSaveTestDeal(databaseUtil);
    }

    @Test
    void saveOrUpdate_newDeal_shouldSaveAndReturnDetails() {
        DealSaveRequest request = dealSaveRequestBuilderFixture().build();

        DealDetails details = dealService.saveOrUpdate(request);

        Deal saved = databaseUtil.find(details.id(), Deal.class);
        assertThat(saved).isNotNull();
        assertDealEqualsRequest(saved, request);
    }

    @Test
    void saveOrUpdate_existingDeal_shouldUpdateAndReturnDetails() {
        DealSaveRequest request = DealSaveRequest.builder()
                .id(existingDeal.getId())
                .description("Updated Description")
                .agreementNumber(existingDeal.getAgreementNumber())
                .build();

        dealService.saveOrUpdate(request);

        Deal updated = databaseUtil.find(request.id(), Deal.class);
        assertThat(updated).isNotNull();
        assertDealEqualsRequest(updated, request);
    }

    @Test
    void changeDealStatus_validRequest_shouldUpdateStatus() {
        DealStatus newStatus = createAndSaveStatus(databaseUtil, "APPROVED");

        DealStatusChangeRequest request = new DealStatusChangeRequest(existingDeal.getId(), newStatus.getId());
        dealService.changeDealStatus(request);

        Deal updated = databaseUtil.find(existingDeal.getId(), Deal.class);
        assertThat(updated.getStatus().getId()).isEqualTo(newStatus.getId());
    }

    @Test
    void changeDealStatus_nonExistingStatus_shouldThrow() {
        DealStatusChangeRequest request = new DealStatusChangeRequest(existingDeal.getId(), "dummy");
        assertThatThrownBy(() -> dealService.changeDealStatus(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void changeDealStatus_nonExistingDeal_shouldThrow() {
        DealStatusChangeRequest request = new DealStatusChangeRequest(randomUUID(), existingDeal.getStatus().getId());
        assertThatThrownBy(() -> dealService.changeDealStatus(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getDealById_existingDeal_shouldReturnDetails() {
        DealDetails details = dealService.getDealById(existingDeal.getId());
        assertThat(details).isNotNull();
        assertThat(details.id()).isEqualTo(existingDeal.getId());
    }

    @Test
    void getDealById_nonExistingDeal_shouldThrow() {
        UUID randomId = randomUUID();
        assertThatThrownBy(() -> dealService.getDealById(randomId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private void assertDealEqualsRequest(Deal deal, DealSaveRequest request) {
        assertThat(deal).isNotNull();
        assertThat(deal.getDescription()).isEqualTo(request.description());
        assertThat(deal.getAgreementNumber()).isEqualTo(request.agreementNumber());
        assertThat(deal.getAgreementDate()).isEqualTo(request.agreementDate());
        assertThat(deal.getAgreementStartDate()).isEqualTo(request.agreementStartDate());
        assertThat(deal.getCloseDate()).isEqualTo(request.closeDate());
        assertThat(deal.getAvailabilityDate()).isEqualTo(request.availabilityDate());
    }
}
