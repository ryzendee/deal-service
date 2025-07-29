package ryzendee.app.rest.ui;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryzendee.app.config.SecurityConfiguration;
import ryzendee.app.dto.DealSaveRequest;
import ryzendee.app.dto.DealSearchFilter;
import ryzendee.app.dto.DealStatusChangeRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.rest.impl.ui.DealUiRestController;
import ryzendee.app.security.DealAccessRules;
import ryzendee.app.service.DealService;
import ryzendee.starter.jwt.config.JwtSecurityAutoConfiguration;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static ryzendee.app.testutils.FixtureUtil.*;

@Import({
        SecurityConfiguration.class,
        JwtSecurityAutoConfiguration.class,
        DealAccessRules.class
})
@WebMvcTest(DealUiRestController.class)
public class DealUiRestControllerTest {

    private static final String BASE_URI = "/ui/deal";

    @MockitoBean
    private DealService dealService;

    @Autowired
    private MockMvc mockMvc;

    private MockMvcRequestSpecification request;
    private UUID dealId;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.basePath = BASE_URI;
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        request = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON);
        dealId = UUID.randomUUID();
    }

    @Test
    void saveOrUpdateDeal_authorizedRole_statusOk() {
        DealSaveRequest saveRequest = dealSaveRequestBuilderFixture().build();

        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.OK);

        verify(dealService).saveOrUpdate(saveRequest);
    }

    @Test
    void saveOrUpdateDeal_unauthorizedRole_statusForbidden() {
        DealSaveRequest saveRequest = dealSaveRequestBuilderFixture().build();

        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealService, never()).saveOrUpdate(saveRequest);
    }

    @Test
    void changeDealStatus_authorizedRole_statusNoContent() {
        DealStatusChangeRequest statusChangeRequest = dealStatusChangeRequestFixture();

        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .body(statusChangeRequest)
                .patch("/change/status")
                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(dealService).changeDealStatus(statusChangeRequest);
    }

    @Test
    void changeDealStatus_unauthorizedRole_statusForbidden() {
        DealStatusChangeRequest statusChangeRequest = dealStatusChangeRequestFixture();

        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .body(statusChangeRequest)
                .patch("/change/status")
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealService, never()).changeDealStatus(statusChangeRequest);
    }

    @Test
    void getDealById_authorizedRole_statusOk() {
        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .get("/{id}", dealId)
                .then()
                .status(HttpStatus.OK);

        verify(dealService).getDealById(dealId);
    }

    @Test
    void getDealById_unauthorizedRole_statusForbidden() {
        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .get("/{id}", dealId)
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealService, never()).getDealById(dealId);
    }

    @Test
    void searchDeals_authorizedRole_statusOk() {
        DealSearchFilter filter = dealSearchFilterFixture();

        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .body(filter)
                .post("/search")
                .then()
                .status(HttpStatus.OK);

        verify(dealService).searchDeals(filter);
    }
    @Test
    void searchDeals_unauthorizedRole_statusForbidden() {
        DealSearchFilter filter = dealSearchFilterFixture();

        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .body(filter)
                .post("/search")
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealService, never()).searchDeals(filter);
    }


    @Test
    void exportDealsToExcel_authorizedRole_statusOk() {
        DealSearchFilter filter = dealSearchFilterFixture();
        when(dealService.exportDetails(filter)).thenReturn(exportResultFixture());

        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .body(filter)
                .post("/search/export")
                .then()
                .status(HttpStatus.OK);

        verify(dealService).exportDetails(filter);
    }

    @Test
    void exportDealsToExcel_unauthorizedRole_statusForbidden() {
        DealSearchFilter filter = dealSearchFilterFixture();

        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .body(filter)
                .post("/search/export")
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealService, never()).exportDetails(filter);
    }
}
