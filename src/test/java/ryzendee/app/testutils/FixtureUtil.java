package ryzendee.app.testutils;

import org.springframework.lang.Contract;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ryzendee.app.dto.*;
import ryzendee.app.models.DealContractor;
import ryzendee.app.util.exporter.ExportResult;
import ryzendee.starter.jwt.auth.JwtAuthenticationToken;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;

public class FixtureUtil {

    public static ExportResult exportResultFixture() {
        String str = "fixture.excel";
        return ExportResult.builder()
                .filename(str)
                .content(str.getBytes())
                .build();
    }
    public static DealContractorRoleRemoveRequest contractorRoleRemoveRequestFixture() {
        return new DealContractorRoleRemoveRequest(randomUUID(), "role");
    }
    public static DealContractorRoleAddRequest contractorRoleAddRequestFixture() {
        return new DealContractorRoleAddRequest(randomUUID(), "role");
    }

    public static DealStatusChangeRequest dealStatusChangeRequestFixture() {
        return new DealStatusChangeRequest(randomUUID(), "status");
    }

    public static DealSearchFilter dealSearchFilterFixture() {
        return new DealSearchFilter(
                UUID.randomUUID(),
                "Sample description",
                "AG-987654",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2023, 2, 1),
                LocalDate.of(2023, 11, 30),
                List.of("type1", "type2"),
                List.of("status1", "status2"),
                LocalDate.of(2023, 5, 1),
                LocalDate.of(2023, 10, 31),
                "borrower example",
                "warranty example",
                0,
                10,
                new DealSearchFilter.SumFilter(new BigDecimal("1000.00"), "USD")
        );
    }

    public static DealSaveRequest.DealSaveRequestBuilder dealSaveRequestBuilderFixture() {
        return DealSaveRequest.builder()
                .description("Default description")
                .agreementNumber("AG-123456")
                .agreementDate(LocalDate.of(2023, 1, 1))
                .agreementStartDate(LocalDate.of(2023, 1, 2).atStartOfDay())
                .availabilityDate(LocalDate.of(2023, 1, 3))
                .typeId("defaultTypeId")
                .closeDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                .sum(new DealSumDetails(BigDecimal.ONE, "test"));
    }

    public static ContractorDetails.ContractorDetailsBuilder contractorDetailsBuilderFixture() {
        return ContractorDetails.builder()
                .id(UUID.randomUUID())
                .contractorId("C123456789")
                .name("Test Contractor")
                .inn("1234567890")
                .main(true)
                .roles(emptyList());
    }

    public static ContractorDetails contractorDetailsFixture() {
        return ContractorDetails.builder()
                .build();
    }

    public static ContractorSaveRequest.ContractorSaveRequestBuilder contractorSaveRequestBuilderFixture() {
        return ContractorSaveRequest.builder()
                .contractorId("contractor")
                .inn("inn")
                .dealId(randomUUID())
                .main(true)
                .name("name");
    }

    public static Authentication authenticationFixture(AuthRole role) {
        return new JwtAuthenticationToken("user", "jwt-token", List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));
    }
}
