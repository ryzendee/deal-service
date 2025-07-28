package ryzendee.app.rest.ui;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryzendee.app.config.SecurityConfiguration;
import ryzendee.app.dto.ContractorSaveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.rest.impl.ui.DealContractorUiRestController;
import ryzendee.app.security.DealAccessRules;
import ryzendee.app.service.DealContractorService;
import ryzendee.starter.jwt.config.JwtSecurityAutoConfiguration;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static ryzendee.app.testutils.FixtureUtil.authenticationFixture;
import static ryzendee.app.testutils.FixtureUtil.contractorSaveRequestBuilderFixture;

@Import({
        SecurityConfiguration.class,
        JwtSecurityAutoConfiguration.class,
        DealAccessRules.class
})
@WebMvcTest(DealContractorUiRestController.class)
public class DealContractorUiRestControllerTest {

    private static final String BASE_URI = "/ui/deal-contractor";

    @MockitoBean
    private DealContractorService dealContractorService;

    @Autowired
    private MockMvc mockMvc;

    private MockMvcRequestSpecification request;
    private UUID contractorId;

    @BeforeEach
    void setUp() {

        RestAssuredMockMvc.basePath = BASE_URI;
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        request = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON);
        contractorId = UUID.randomUUID();
    }

    @Test
    void saveOrUpdateDealContractor_authorizedRole_statusOk() {
        ContractorSaveRequest saveRequest = contractorSaveRequestBuilderFixture().build();

        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.OK);

        verify(dealContractorService).saveOrUpdate(saveRequest);
    }

    @Test
    void saveOrUpdateDealContractor_unauthorizedRole_statusForbidden() {
        ContractorSaveRequest saveRequest = contractorSaveRequestBuilderFixture().build();

        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealContractorService, never()).saveOrUpdate(saveRequest);
    }

    @Test
    void deleteDealContractor_authorizedRole_statusNoContent() {
        doNothing().when(dealContractorService).deleteById(contractorId);

        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .queryParam("id", contractorId)
                .delete("/delete")
                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(dealContractorService).deleteById(contractorId);
    }

    @Test
    void deleteDealContractor_unauthorizedRole_statusNoContent() {
        doNothing().when(dealContractorService).deleteById(contractorId);

        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .queryParam("id", contractorId)
                .delete("/delete")
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealContractorService, never()).deleteById(contractorId);
    }
}
