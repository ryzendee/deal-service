package ryzendee.app.service;

import ryzendee.app.models.*;
import ryzendee.app.testutils.DatabaseUtil;

import java.util.UUID;

import static java.util.UUID.randomUUID;

class DealTestHelper {

    private static final int LENGTH = 20;

    public static Deal createAndSaveTestDeal(DatabaseUtil databaseUtil) {
        DealType dealType = createAndSaveType(databaseUtil);
        DealStatus status = createAndSaveStatus(databaseUtil, "DRAFT");

        Deal deal = Deal.builder()
                .description("Test Deal")
                .agreementNumber("AG123")
                .type(dealType)
                .status(status)
                .isActive(true)
                .build();

        return databaseUtil.save(deal);
    }

    public static DealStatus createAndSaveStatus(DatabaseUtil databaseUtil, String name) {
        DealStatus status = DealStatus.builder()
                .id(generateStringId())
                .name(name)
                .build();
        return databaseUtil.save(status);
    }

    public static DealType createAndSaveType(DatabaseUtil databaseUtil) {
        DealType type = DealType.builder()
                .id(generateStringId())
                .name("Standard")
                .build();
        return databaseUtil.save(type);
    }

    public static DealContractor createAndSaveContractor(DatabaseUtil databaseUtil, Deal deal) {
        DealContractor contractor = DealContractor.builder()
                .deal(deal)
                .contractorId("CONTR-TEST")
                .name("Initial Contractor")
                .inn("1231231234")
                .main(true)
                .isActive(true)
                .build();
        return databaseUtil.save(contractor);
    }

    public static ContractorRole createAndSaveRole(DatabaseUtil databaseUtil) {
        ContractorRole role = ContractorRole.builder()
                .id(generateStringId())
                .name("role")
                .category("category")
                .isActive(true)
                .build();

        return databaseUtil.save(role);
    }

    private static String generateStringId() {
        return randomUUID().toString().substring(0, LENGTH);
    }
}
