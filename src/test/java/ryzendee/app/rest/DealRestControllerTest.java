package ryzendee.app.rest;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ryzendee.app.dto.*;
import ryzendee.app.exception.ResourceNotFoundException;
import ryzendee.app.rest.DealRestController;
import ryzendee.app.service.DealService;
import ryzendee.app.service.impl.DealServiceImpl;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static ryzendee.app.testutils.FixtureUtil.*;

@WebMvcTest(DealRestController.class)
public class DealRestControllerTest {

    private static final String BASE_URI = "/deal";

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
    void saveOrUpdateDeal_validRequest_statusOk() {
        DealSaveRequest saveRequest = dealSaveRequestBuilderFixture().build();

        request.body(saveRequest)
                .put("/save")
                .then()
                .status(HttpStatus.OK);

        verify(dealService).saveOrUpdate(saveRequest);
    }

    @Test
    void changeDealStatus_validRequest_statusNoContent() {
        DealStatusChangeRequest statusChangeRequest = dealStatusChangeRequestFixture();

        request.body(statusChangeRequest)
                .patch("/change/status")
                .then()
                .status(HttpStatus.NO_CONTENT);

        verify(dealService).changeDealStatus(statusChangeRequest);
    }

    @Test
    void changeDealStatus_nonExistingDeal_statusNotFound() {
        DealStatusChangeRequest statusChangeRequest = dealStatusChangeRequestFixture();

        doThrow(ResourceNotFoundException.class).when(dealService).changeDealStatus(statusChangeRequest);

        request.body(statusChangeRequest)
                .patch("/change/status")
                .then()
                .status(HttpStatus.NOT_FOUND);

        verify(dealService).changeDealStatus(statusChangeRequest);
    }

    @Test
    void getDealById_existingId_statusOk() {
        request.get("/{id}", dealId)
                .then()
                .status(HttpStatus.OK);

        verify(dealService).getDealById(dealId);
    }

    @Test
    void getDealById_nonExistingId_statusNotFound() {
        doThrow(ResourceNotFoundException.class).when(dealService).getDealById(dealId);

        request.get("/{id}", dealId)
                .then()
                .status(HttpStatus.NOT_FOUND);

        verify(dealService).getDealById(dealId);
    }

    @Test
    void searchDeals_validFilter_statusOk() {
        DealSearchFilter filter = dealSearchFilterFixture();

        request.body(filter)
                .post("/search")
                .then()
                .status(HttpStatus.OK);

        verify(dealService).searchDeals(filter);
    }

    @Test
    void exportDealsToExcel_validFilter_statusOk() {
        DealSearchFilter filter = dealSearchFilterFixture();
        when(dealService.exportDetails(filter)).thenReturn(exportResultFixture());

        request.body(filter)
                .post("/search/export")
                .then()
                .status(HttpStatus.OK);

        verify(dealService).exportDetails(filter);
    }
}
