package ryzendee.app.rest.base;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryzendee.app.dto.DealContractorRoleAddRequest;
import ryzendee.app.dto.DealContractorRoleRemoveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.rest.impl.base.DealContractorToRoleRestController;
import ryzendee.app.service.DealContractorToRoleService;

import static org.mockito.Mockito.*;
import static ryzendee.app.testutils.FixtureUtil.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(DealContractorToRoleRestController.class)
public class DealContractorToRoleRestControllerTest {

    private static final String BASE_URI = "/contractor-to-role";

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
    void addRoleToContractor_validRequest_statusOk() {
        DealContractorRoleAddRequest addRequest = contractorRoleAddRequestFixture();

        request.body(addRequest)
                .post("/add")
                .then()
                .status(HttpStatus.OK);

        verify(dealContractorToRoleService).addRoleToContractor(addRequest);
    }

    @Test
    void deleteRoleFromContractor_existingRole_statusNoContent() {
        DealContractorRoleRemoveRequest removeRequest = contractorRoleRemoveRequestFixture();

        request.body(removeRequest)
                .delete("/delete")
                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(dealContractorToRoleService).deleteRole(removeRequest);
    }

    @Test
    void deleteRoleFromContractor_nonExistingRole_statusNotFound() {
        DealContractorRoleRemoveRequest removeRequest = contractorRoleRemoveRequestFixture();

        doThrow(ResourceNotFoundException.class)
                .when(dealContractorToRoleService).deleteRole(removeRequest);

        request.body(removeRequest)
                .delete("/delete")
                .then()
                .status(HttpStatus.NOT_FOUND);

        verify(dealContractorToRoleService).deleteRole(removeRequest);
    }
}
