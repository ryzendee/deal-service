package ryzendee.app.rest.ui;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryzendee.app.config.SecurityConfiguration;
import ryzendee.app.dto.DealContractorRoleAddRequest;
import ryzendee.app.dto.DealContractorRoleRemoveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.rest.impl.base.DealContractorToRoleRestController;
import ryzendee.app.rest.impl.ui.DealContractorToRoleUiRestController;
import ryzendee.app.security.DealAccessRules;
import ryzendee.app.service.DealContractorToRoleService;
import ryzendee.starter.jwt.config.JwtSecurityAutoConfiguration;
import ryzendee.starter.jwt.decoder.AuthRole;

import static org.mockito.Mockito.*;
import static ryzendee.app.testutils.FixtureUtil.*;

@Import({
        SecurityConfiguration.class,
        JwtSecurityAutoConfiguration.class,
        DealAccessRules.class
})
@WebMvcTest(DealContractorToRoleUiRestController.class)
public class DealContractorToRoleUiRestControllerTest {

    private static final String BASE_URI = "/ui/contractor-to-role";

    @MockitoBean
    private DealContractorToRoleService dealContractorToRoleService;

    @Autowired
    private MockMvc mockMvc;

    private MockMvcRequestSpecification request;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.basePath = BASE_URI;
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        request = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON);
    }

    @Test
    void addRoleToContractor_unauthorizedRole_statusForbidden() {
        DealContractorRoleAddRequest addRequest = contractorRoleAddRequestFixture();

        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .body(addRequest)
                .post("/add")
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealContractorToRoleService, never()).addRoleToContractor(addRequest);
    }

    @Test
    void addRoleToContractor_authorizedRole_statusOk() {
        DealContractorRoleAddRequest addRequest = contractorRoleAddRequestFixture();

        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .body(addRequest)
                .post("/add")
                .then()
                .status(HttpStatus.OK);

        verify(dealContractorToRoleService).addRoleToContractor(addRequest);
    }

    @Test
    void deleteRoleFromContractor_authorizedRole_statusNoContent() {
        DealContractorRoleRemoveRequest removeRequest = contractorRoleRemoveRequestFixture();

        request.auth().authentication(authenticationFixture(AuthRole.SUPERUSER))
                .body(removeRequest)
                .delete("/delete")
                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(dealContractorToRoleService).deleteRole(removeRequest);
    }

    @Test
    void deleteRoleFromContractor_unauthorizedRole_statusForbidden() {
        DealContractorRoleRemoveRequest removeRequest = contractorRoleRemoveRequestFixture();

        request.auth().authentication(authenticationFixture(AuthRole.USER))
                .body(removeRequest)
                .delete("/delete")
                .then()
                .status(HttpStatus.FORBIDDEN);

        verify(dealContractorToRoleService, never()).deleteRole(removeRequest);
    }
}
