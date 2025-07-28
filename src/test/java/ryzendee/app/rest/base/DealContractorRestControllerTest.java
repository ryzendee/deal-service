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
import ryzendee.app.dto.ContractorSaveRequest;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.rest.impl.base.DealContractorRestController;
import ryzendee.app.service.DealContractorService;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static ryzendee.app.testutils.FixtureUtil.contractorSaveRequestBuilderFixture;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(DealContractorRestController.class)
public class DealContractorRestControllerTest {

    private static final String BASE_URI = "/deal-contractor";

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
    void saveOrUpdateDealContractor_validRequest_statusOk() {
        ContractorSaveRequest saveRequest = contractorSaveRequestBuilderFixture().build();

        request.body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.OK);

        verify(dealContractorService).saveOrUpdate(saveRequest);
    }

    @Test
    void deleteDealContractor_existingId_statusNoContent() {
        doNothing().when(dealContractorService).deleteById(contractorId);

        request.queryParam("id", contractorId)
                .delete("/delete")
                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(dealContractorService).deleteById(contractorId);
    }

    @Test
    void deleteDealContractor_nonExistingId_statusNotFound() {
        doThrow(ResourceNotFoundException.class).when(dealContractorService).deleteById(contractorId);

        request.queryParam("id", contractorId)
                .delete("/delete")
                .then()
                .status(HttpStatus.NOT_FOUND);

        verify(dealContractorService).deleteById(contractorId);
    }
}
